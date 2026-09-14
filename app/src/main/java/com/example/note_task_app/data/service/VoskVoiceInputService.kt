package com.example.note_task_app.data.service

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.core.content.ContextCompat
import com.example.note_task_app.domain.model.VoiceEvent
import com.example.note_task_app.domain.service.VoiceInputService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
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

    @Volatile
    private var isRecording = false

    private val modelAssetPath = "vosk-model-small-ru-0.22"
    private val voskDispatcher = Dispatchers.IO.limitedParallelism(1)
    private val model: Model by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        provideModelPath()
    }

    private fun provideModelPath(): Model {
        val modelDir = File(context.filesDir, modelAssetPath)
        if (!modelDir.exists() || modelDir.listFiles()?.isEmpty() == true) {
            copyModelFromAssets()
        }
        val hasModelFiles = modelDir.listFiles()?.any {
            it.isFile && (it.name.endsWith(".am") || it.name == "conf" || it.name == "ivector")
        } == true
        if (!hasModelFiles) {
            modelDir.deleteRecursively()
            copyModelFromAssets()
        }
        return Model(modelDir.absolutePath)
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
            val childItems = assetManager.list(assetItemPath)
            if (childItems?.isNotEmpty() ?: false) {
                destFile.mkdirs()
                copyAssetsRecursively(assetItemPath, destFile)
            } else {
                assetManager.open(assetItemPath).use { input ->
                    FileOutputStream(destFile).use { output ->
                        input.copyTo(output)
                    }
                }
            }
        }
    }

    override fun startListening(): Flow<VoiceEvent> = callbackFlow {
        val workerJob: Job = launch(voskDispatcher) {
            var localRecognizer: Recognizer? = null
            var localAudioRecord: AudioRecord? = null

            try {
                // 1. Проверка разрешения
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    send(VoiceEvent.Error("Нет разрешения на запись аудио"))
                    close()
                    return@launch
                }

                // 2. Модель
                val loadedModel = try { model } catch (e: Exception) {
                    send(VoiceEvent.Error("Не удалось загрузить модель распознавания"))
                    close()
                    return@launch
                }

                // 3. AudioRecord
                val sampleRate = 16000
                val channelConfig = AudioFormat.CHANNEL_IN_MONO
                val audioFormat = AudioFormat.ENCODING_PCM_16BIT
                val bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)
                if (bufferSize == AudioRecord.ERROR || bufferSize == AudioRecord.ERROR_BAD_VALUE) {
                    send(VoiceEvent.Error("Ошибка инициализации аудио"))
                    close()
                    return@launch
                }

                localAudioRecord = AudioRecord(
                    MediaRecorder.AudioSource.MIC,
                    sampleRate,
                    channelConfig,
                    audioFormat,
                    bufferSize * 2
                )
                audioRecord = localAudioRecord

                if (localAudioRecord.state != AudioRecord.STATE_INITIALIZED) {
                    send(VoiceEvent.Error("Не удалось инициализировать микрофон"))
                    close()
                    return@launch
                }

                // 4. Recognizer — создаём здесь же, в том же потоке
                localRecognizer = Recognizer(loadedModel, sampleRate.toFloat())
                recognizer = localRecognizer

                isRecording = true
                send(VoiceEvent.Ready)
                send(VoiceEvent.Listening)

                localAudioRecord.startRecording()
                val audioData = ByteArray(bufferSize)

                while (isRecording && isActive) {
                    val readCount = localAudioRecord.read(audioData, 0, audioData.size)
                    if (readCount > 0) {
                        if (localRecognizer.acceptWaveForm(audioData, readCount)) {
                            val text = extractTextFromResult(localRecognizer.result)
                            if (text.isNotEmpty()) {
                                send(VoiceEvent.FinalResult(text))
                                break
                            }
                        } else {
                            val partialText = extractTextFromPartialResult(localRecognizer.partialResult)
                            if (partialText.isNotEmpty()) {
                                send(VoiceEvent.PartialResult(partialText))
                            }
                        }
                    }
                }
            } catch (e: SecurityException) {
                send(VoiceEvent.Error("Нет разрешения на запись аудио"))
            } catch (e: Exception) {
                // Игнорируем CancellationException — это нормальная отмена
                if (e !is kotlinx.coroutines.CancellationException) {
                    send(VoiceEvent.Error("Ошибка записи: ${e.message}"))
                }
            } finally {
                runCatching { localAudioRecord?.stop() }
                runCatching { localAudioRecord?.release() }
                runCatching { localRecognizer?.close() }
                audioRecord = null
                recognizer = null
                isRecording = false
                close()
            }
        }

        awaitClose {
            isRecording = false
            runCatching { audioRecord?.stop() }
            workerJob.cancel()
        }
    }

    override fun stopListening() {
        isRecording = false
        runCatching { audioRecord?.stop() }
    }

    private fun extractTextFromResult(json: String): String =
        runCatching { JSONObject(json).getString("text") }.getOrDefault("")

    private fun extractTextFromPartialResult(json: String): String =
        runCatching { JSONObject(json).getString("partial") }.getOrDefault("")
}