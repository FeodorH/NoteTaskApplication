package com.example.note_task_app.domain.usecases

import com.example.note_task_app.domain.service.BackendService
import javax.inject.Inject

class GigaChatGenerateTaskUsingByVoiceUseCase @Inject constructor(
    private val service: BackendService
) {
    suspend operator fun invoke(voiceText: String): String? =
        service.generateTaskFromVoice(voiceText)
}