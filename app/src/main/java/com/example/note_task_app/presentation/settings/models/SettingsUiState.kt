package com.example.note_task_app.presentation.settings.models

import com.example.note_task_app.domain.model.AppColorScheme
import com.example.note_task_app.domain.model.ThemeMode

data class SettingsUiState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val colorScheme: AppColorScheme = AppColorScheme.Default,
    val balance: Double? = null,
    val isBalanceLoading: Boolean = false,
    val error: String? = null
)