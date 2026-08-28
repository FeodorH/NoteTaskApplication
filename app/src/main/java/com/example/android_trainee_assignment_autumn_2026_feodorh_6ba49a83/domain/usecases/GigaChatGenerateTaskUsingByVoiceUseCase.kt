package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases

import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.service.GigaChatService
import javax.inject.Inject

class GigaChatGenerateTaskUsingByVoiceUseCase @Inject constructor(
    private val service: GigaChatService
) {
    suspend operator fun invoke(voiceText: String): String? =
        service.generateTaskFromVoice(voiceText)
}