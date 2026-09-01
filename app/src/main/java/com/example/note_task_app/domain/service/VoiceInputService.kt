package com.example.note_task_app.domain.service

import kotlinx.coroutines.flow.Flow

interface VoiceInputService {
    fun startListening(): Flow<VoiceEvent>
    fun stopListening()
}

sealed class VoiceEvent {
    object Ready : VoiceEvent()
    object Listening : VoiceEvent()
    data class PartialResult(val text: String) : VoiceEvent()
    data class FinalResult(val text: String) : VoiceEvent()
    data class Error(val message: String) : VoiceEvent()
    object Cancelled : VoiceEvent()
}