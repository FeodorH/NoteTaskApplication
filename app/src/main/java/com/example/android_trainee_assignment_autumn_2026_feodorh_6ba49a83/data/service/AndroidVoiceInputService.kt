package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.service

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.service.VoiceEvent
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.service.VoiceInputService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidVoiceInputService @Inject constructor(
    @ApplicationContext private val context: Context
) : VoiceInputService {

    private var speechRecognizer: SpeechRecognizer? = null
    private var isListening = false

    override fun startListening(): Flow<VoiceEvent> = callbackFlow {
        val recognizer = SpeechRecognizer.createSpeechRecognizer(context)
        speechRecognizer = recognizer

        val listener = object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                Log.d("VoiceInput", "onReadyForSpeech")
                trySend(VoiceEvent.Ready)
            }

            override fun onBeginningOfSpeech() {
                trySend(VoiceEvent.Listening)
                isListening = true
            }

            override fun onRmsChanged(rmsdB: Float) {}

            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {
                isListening = false
            }

            override fun onError(error: Int) {
                val message = when (error) {
                    SpeechRecognizer.ERROR_AUDIO -> "Ошибка аудио"
                    SpeechRecognizer.ERROR_CLIENT -> "Ошибка клиента"
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Нет разрешения на микрофон"
                    SpeechRecognizer.ERROR_NETWORK -> "Ошибка сети"
                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Тайм-аут сети"
                    SpeechRecognizer.ERROR_NO_MATCH -> "Речь не распознана"
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Распознаватель занят"
                    SpeechRecognizer.ERROR_SERVER -> "Ошибка сервера"
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Речь не обнаружена"
                    else -> "Ошибка распознавания ($error)"
                }
                Log.e("VoiceInput", error.toString())
                trySend(VoiceEvent.Error(message))
                close()
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val text = matches?.firstOrNull() ?: ""
                if (text.isNotEmpty()) {
                    trySend(VoiceEvent.FinalResult(text))
                } else {
                    trySend(VoiceEvent.Error("Речь не распознана"))
                }
                close()
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val matches =
                    partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val text = matches?.firstOrNull() ?: ""
                if (text.isNotEmpty()) {
                    trySend(VoiceEvent.PartialResult(text))
                }
            }

            override fun onEvent(eventType: Int, params: Bundle?) {}
        }

        recognizer.setRecognitionListener(listener)

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }

        recognizer.startListening(intent)

        awaitClose {
            recognizer.stopListening()
            recognizer.destroy()
            speechRecognizer = null
            isListening = false
        }
    }

    override fun stopListening() {
        speechRecognizer?.stopListening()
        isListening = false
    }
}