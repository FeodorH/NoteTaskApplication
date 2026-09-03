package com.example.note_task_app.domain.model

sealed class VoiceEvent {
    object Ready : VoiceEvent()
    object Listening : VoiceEvent()
    data class PartialResult(val text: String) : VoiceEvent()
    data class FinalResult(val text: String) : VoiceEvent()
    data class Error(val message: String) : VoiceEvent()
    object Cancelled : VoiceEvent()
}