package com.example.note_task_app.domain.service

import com.example.note_task_app.domain.model.VoiceEvent
import kotlinx.coroutines.flow.Flow

interface VoiceInputService {
    fun startListening(): Flow<VoiceEvent>
    fun stopListening()
}
