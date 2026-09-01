package com.example.note_task_app.domain.usecases

import com.example.note_task_app.domain.service.GigaChatService
import javax.inject.Inject

class GigaChatGetBalanceUseCase @Inject constructor(
    private val service: GigaChatService
) {
    suspend operator fun invoke(): Double? = service.getBalance()
}