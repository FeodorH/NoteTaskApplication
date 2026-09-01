package com.example.note_task_app.domain.usecases

import com.example.note_task_app.domain.model.AppColorScheme
import com.example.note_task_app.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetColorSchemeUseCase @Inject constructor(
    val repository: SettingsRepository
) {
    operator fun invoke(): Flow<AppColorScheme> = repository.getColorScheme()
}