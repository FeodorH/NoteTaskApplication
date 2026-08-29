package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model

data class AppColorScheme(
    val id: String,
    val name: String,
    val primary: Int,
    val onPrimary: Int,
    val secondary: Int,
    val onSecondary: Int
) {
    companion object {
        val Default = AppColorScheme(
            id = "default",
            name = "По умолчанию",
            primary = 0xFF6200EE.toInt(),
            onPrimary = 0xFFFFFFFF.toInt(),
            secondary = 0xFF03DAC6.toInt(),
            onSecondary = 0xFF000000.toInt()
        )

        val PresetColors = listOf(
            Default,
            AppColorScheme(
                id = "blue",
                name = "Синий",
                primary = 0xFF1E88E5.toInt(),
                onPrimary = 0xFFFFFFFF.toInt(),
                secondary = 0xFF64B5F6.toInt(),
                onSecondary = 0xFF000000.toInt()
            ),
            AppColorScheme(
                id = "red",
                name = "Красный",
                primary = 0xFFE53935.toInt(),
                onPrimary = 0xFFFFFFFF.toInt(),
                secondary = 0xFFEF5350.toInt(),
                onSecondary = 0xFF000000.toInt()
            ),
            AppColorScheme(
                id = "green",
                name = "Зелёный",
                primary = 0xFF43A047.toInt(),
                onPrimary = 0xFFFFFFFF.toInt(),
                secondary = 0xFF66BB6A.toInt(),
                onSecondary = 0xFF000000.toInt()
            ),
            AppColorScheme(
                id = "orange",
                name = "Оранжевый",
                primary = 0xFFFB8C00.toInt(),
                onPrimary = 0xFFFFFFFF.toInt(),
                secondary = 0xFFFFA726.toInt(),
                onSecondary = 0xFF000000.toInt()
            ),
            AppColorScheme(
                id = "purple",
                name = "Фиолетовый",
                primary = 0xFF8E24AA.toInt(),
                onPrimary = 0xFFFFFFFF.toInt(),
                secondary = 0xFFCE93D8.toInt(),
                onSecondary = 0xFF000000.toInt()
            )
        )
    }
}