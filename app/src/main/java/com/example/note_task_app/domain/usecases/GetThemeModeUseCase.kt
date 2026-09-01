package com.example.note_task_app.domain.usecases

import com.example.note_task_app.domain.model.ThemeMode
import com.example.note_task_app.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetThemeModeUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<ThemeMode> = repository.getThemeMode()
}