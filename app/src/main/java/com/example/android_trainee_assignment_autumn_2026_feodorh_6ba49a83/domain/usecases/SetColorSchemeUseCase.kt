package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases

import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model.AppColorScheme
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.repository.SettingsRepository
import javax.inject.Inject

class SetColorSchemeUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(scheme: AppColorScheme) = repository.saveColorScheme(scheme)
}