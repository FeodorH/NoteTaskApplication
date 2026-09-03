package com.example.note_task_app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.note_task_app.domain.model.AppColorScheme
import com.example.note_task_app.domain.model.ThemeMode

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
            primaryContainer = Color(colorScheme.primaryContainer),
            onPrimaryContainer = Color(colorScheme.onPrimaryContainer),
            secondary = Color(colorScheme.secondary),
            onSecondary = Color(colorScheme.onSecondary)
        )
    } else {
        lightColorScheme(
            primary = Color(colorScheme.primary),
            onPrimary = Color(colorScheme.onPrimary),
            primaryContainer = Color(colorScheme.primaryContainer),
            onPrimaryContainer = Color(colorScheme.onPrimaryContainer),
            secondary = Color(colorScheme.secondary),
            onSecondary = Color(colorScheme.onSecondary)
        )
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography(),
        content = content
    )
}