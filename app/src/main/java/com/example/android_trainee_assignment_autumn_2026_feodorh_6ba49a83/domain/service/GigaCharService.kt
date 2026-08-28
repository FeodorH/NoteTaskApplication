package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.service

import kotlinx.coroutines.flow.StateFlow

interface GigaChatService {
    val balance: StateFlow<Double?>

    suspend fun getBalance(): Double?
    suspend fun generateTaskFromVoice(voiceText: String): String?
}