package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model.AppColorScheme
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model.ThemeMode

@Composable
fun AppTheme(
    themeMode: ThemeMode,
    colorScheme: AppColorScheme,
    content: @Composable () -> Unit
) {
    val isDarkTheme = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val colors = if (isDarkTheme) {
        darkColorScheme(
            primary = Color(colorScheme.primary),
            onPrimary = Color(colorScheme.onPrimary),
            secondary = Color(colorScheme.secondary),
            onSecondary = Color(colorScheme.onSecondary),
        )
    } else {
        lightColorScheme(
            primary = Color(colorScheme.primary),
            onPrimary = Color(colorScheme.onPrimary),
            secondary = Color(colorScheme.secondary),
            onSecondary = Color(colorScheme.onSecondary),
        )
    }

    MaterialTheme(
        colorScheme = colors,
        typography = androidx.compose.material3.Typography(),
        content = content
    )
}