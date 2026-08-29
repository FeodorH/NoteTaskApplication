package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.local.datastore.dataStore
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model.AppColorScheme
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model.ThemeMode
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.repository.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SettingsRepository {

    companion object {
        private val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
        private val COLOR_SCHEME_ID_KEY = stringPreferencesKey("color_scheme_id")
    }

    override fun getThemeMode(): Flow<ThemeMode> {
        return context.dataStore.data.map { preferences ->
            val mode = preferences[THEME_MODE_KEY] ?: ThemeMode.SYSTEM
            ThemeMode.valueOf(mode as? String ?: "SYSTEM")
        }
    }

    override fun getColorScheme(): Flow<AppColorScheme> {
        return context.dataStore.data.map { preferences ->
            val id = preferences[COLOR_SCHEME_ID_KEY] ?: AppColorScheme.Default.id
            AppColorScheme.PresetColors.find { it.id == id } ?: AppColorScheme.Default
        }
    }

    override suspend fun saveThemeMode(mode: ThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[THEME_MODE_KEY] = mode.name
        }
    }

    override suspend fun saveColorScheme(scheme: AppColorScheme) {
        context.dataStore.edit { preferences ->
            preferences[COLOR_SCHEME_ID_KEY] = scheme.id
        }
    }

    override suspend fun resetSettings() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}