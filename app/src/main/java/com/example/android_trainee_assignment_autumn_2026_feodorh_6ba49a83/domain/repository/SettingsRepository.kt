package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.repository

import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model.AppColorScheme
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getThemeMode(): Flow<ThemeMode>
    fun getColorScheme(): Flow<AppColorScheme>
    suspend fun saveThemeMode(mode: ThemeMode)
    suspend fun saveColorScheme(scheme: AppColorScheme)
    suspend fun resetSettings()
}