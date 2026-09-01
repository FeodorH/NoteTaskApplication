package com.example.note_task_app.domain.usecases

import com.example.note_task_app.domain.model.ThemeMode
import com.example.note_task_app.domain.repository.SettingsRepository
import javax.inject.Inject

class SetThemeModeUseCase @Inject constructor(
    val repository: SettingsRepository
) {
    suspend operator fun invoke(theme: ThemeMode) {
        repository.saveThemeMode(theme)
    }
}