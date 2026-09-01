package com.example.note_task_app.domain.repository

import com.example.note_task_app.domain.model.AppColorScheme
import com.example.note_task_app.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getThemeMode(): Flow<ThemeMode>
    fun getColorScheme(): Flow<AppColorScheme>
    suspend fun saveThemeMode(mode: ThemeMode)
    suspend fun saveColorScheme(scheme: AppColorScheme)
    suspend fun resetSettings()
}