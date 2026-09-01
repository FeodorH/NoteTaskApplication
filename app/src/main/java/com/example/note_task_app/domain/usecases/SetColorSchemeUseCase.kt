package com.example.note_task_app.domain.usecases

import com.example.note_task_app.domain.model.AppColorScheme
import com.example.note_task_app.domain.repository.SettingsRepository
import javax.inject.Inject

class SetColorSchemeUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(scheme: AppColorScheme) = repository.saveColorScheme(scheme)
}