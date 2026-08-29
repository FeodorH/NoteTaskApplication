package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.settings.models

import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model.AppColorScheme
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model.ThemeMode

data class SettingsUiState (
    val themeMode : ThemeMode = ThemeMode.SYSTEM,
    val colorScheme: AppColorScheme = AppColorScheme.Default,
    val balance : Double? = null,
    val isBalanceLoading : Boolean = false,
    val error : String? = null
)