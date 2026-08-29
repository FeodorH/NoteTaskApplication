package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.service

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.core.content.ContextCompat
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.service.VoiceEvent
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.service.VoiceInputService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.vosk.Model
import org.vosk.Recognizer
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VoskVoiceInputService @Inject constructor(
    @ApplicationContext private val context: Context
) : VoiceInputService {

    private var recognizer: Recognizer? = null
    private var audioRecord: AudioRecord? = null
    private var isRecording = false

    private val modelAssetPath = "vosk-model-small-ru-0.22"
    private var model: Model? = null

    private suspend fun initModel(): Boolean = withContext(Dispatchers.IO) {
        try {
            val modelDir = File(context.filesDir, modelAssetPath)
            // Если папка не существует или пуста — копируем
            if (!modelDir.exists() || modelDir.listFiles()?.isEmpty() == true) {
                copyModelFromAssets()
            }
            // Проверяем наличие ключевых файлов модели (например, am или conf)
            val hasModelFiles = modelDir.listFiles()?.any {
                it.isFile && (it.name.endsWith(".am") || it.name == "conf" || it.name == "ivector")
            } == true
            if (!hasModelFiles) {
                // Если файлов нет, возможно, модель не скопировалась — пытаемся ещё раз
                modelDir.deleteRecursively()
                copyModelFromAssets()
            }
            model = Model(modelDir.absolutePath)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun copyModelFromAssets() {
        val modelDir = File(context.filesDir, modelAssetPath)
        modelDir.mkdirs()
        copyAssetsRecursively(modelAssetPath, modelDir)
    }

    private fun copyAssetsRecursively(assetPath: String, destinationDir: File) {
        val assetManager = context.assets
        val items = assetManager.list(assetPath) ?: return
        for (item in items) {
            val assetItemPath = "$assetPath/$item"
            val destFile = File(destinationDir, item)
            // Проверяем, является ли элемент папкой
            val childItems = assetManager.list(assetItemPath)
            if (childItems?.isNotEmpty() ?: false) {
                // Это папка
                destFile.mkdirs()
                copyAssetsRecursively(assetItemPath, destFile)
            } else {
                // Это файл
                assetManager.open(assetItemPath).use { input ->
                    FileOutputStream(destFile).use { output ->
                        input.copyTo(output)
                    }
                }
            }
        }
    }

    override fun startListening(): Flow<VoiceEvent> = callbackFlow {
        withContext(Dispatchers.IO) {
            // Проверка разрешения
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED
            ) {
                send(VoiceEvent.Error("Нет разрешения на запись аудио"))
                close()
                return@withContext
            }

            // Инициализация модели
            val modelReady = initModel()
            if (!modelReady || model == null) {
                send(VoiceEvent.Error("Не удалось загрузить модель распознавания"))
                close()
                return@withContext
            }

            val sampleRate = 16000
            val channelConfig = AudioFormat.CHANNEL_IN_MONO
            val audioFormat = AudioFormat.ENCODING_PCM_16BIT
            val bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)
            if (bufferSize == AudioRecord.ERROR || bufferSize == AudioRecord.ERROR_BAD_VALUE) {
                send(VoiceEvent.Error("Ошибка инициализации аудио"))
                close()
                return@withContext
            }

            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                channelConfig,
                audioFormat,
                bufferSize * 2
            )

            if (audioRecord?.state != AudioRecord.STATE_INITIALIZED) {
                send(VoiceEvent.Error("Не удалось инициализировать микрофон"))
                close()
                return@withContext
            }

            val recognizerInstance = Recognizer(model, sampleRate.toFloat())
            recognizer = recognizerInstance
            isRecording = true

            send(VoiceEvent.Ready)
            send(VoiceEvent.Listening)

            val audioData = ByteArray(bufferSize)

            try {
                audioRecord?.startRecording()
                while (isRecording && !isClosedForSend) {
                    val readCount = audioRecord?.read(audioData, 0, audioData.size) ?: 0
                    if (readCount > 0) {
                        if (recognizerInstance.acceptWaveForm(audioData, readCount)) {
                            val result = recognizerInstance.result
                            val text = extractTextFromResult(result)
                            if (text.isNotEmpty()) {
                                send(VoiceEvent.FinalResult(text))
                                break
                            }
                        } else {
                            val partialResult = recognizerInstance.partialResult
                            val partialText = extractTextFromPartialResult(partialResult)
                            if (partialText.isNotEmpty()) {
                                send(VoiceEvent.PartialResult(partialText))
                            }
                        }
                    }
                }
            } catch (e: SecurityException) {
                send(VoiceEvent.Error("Нет разрешения на запись аудио"))
            } catch (e: Exception) {
                send(VoiceEvent.Error("Ошибка записи: ${e.message}"))
            } finally {
                audioRecord?.stop()
                audioRecord?.release()
                audioRecord = null
                isRecording = false
                recognizerInstance.close()
                recognizer = null
                close()
            }
        }

        awaitClose {
            stopListening()
        }
    }

    override fun stopListening() {
        isRecording = false
        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null
        recognizer?.close()
        recognizer = null
    }

    private fun extractTextFromResult(json: String): String {
        return try {
            JSONObject(json).getString("text")
        } catch (e: Exception) {
            ""
        }
    }

    private fun extractTextFromPartialResult(json: String): String {
        return try {
            JSONObject(json).getString("partial")
        } catch (e: Exception) {
            ""
        }
    }
}