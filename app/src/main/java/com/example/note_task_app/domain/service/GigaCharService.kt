package com.example.note_task_app.domain.service

import kotlinx.coroutines.flow.StateFlow

interface GigaChatService {
    val balance: StateFlow<Double?>

    suspend fun getBalance(): Double?
    suspend fun generateTaskFromVoice(voiceText: String): String?
}