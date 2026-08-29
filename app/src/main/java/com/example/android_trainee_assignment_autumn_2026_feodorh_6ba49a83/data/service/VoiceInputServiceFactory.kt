package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.service

import android.content.Context
import android.speech.SpeechRecognizer
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.service.VoiceInputService
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class VoiceInputServiceFactory @Inject constructor(
    @ApplicationContext private val context: Context,
    private val googleService: AndroidVoiceInputService,
    private val voskVoiceInputService: VoskVoiceInputService
) {

    fun getService(): VoiceInputService {
        return if (SpeechRecognizer.isRecognitionAvailable(context)) {
            googleService
        } else {
            voskVoiceInputService
        }
    }
}