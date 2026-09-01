package com.example.note_task_app.domain.usecases

import com.example.note_task_app.domain.repository.SettingsRepository
import javax.inject.Inject

class ResetSettingsUseCase @Inject constructor(
    val repository: SettingsRepository
) {
    suspend operator fun invoke() {
        repository.resetSettings()
    }
}