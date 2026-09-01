package com.example.note_task_app.domain.model

data class AppColorScheme(
    val id: String,
    val name: String,
    val primary: Int,
    val onPrimary: Int,
    val primaryContainer: Int,
    val onPrimaryContainer: Int,
    val secondary: Int,
    val onSecondary: Int,
    val tertiary: Int,
    val onTertiary: Int
) {
    companion object {
        val Default = AppColorScheme(
            id = "default",
            name = "По умолчанию",
            primary = 0xFF6200EE.toInt(),
            onPrimary = 0xFFFFFFFF.toInt(),
            primaryContainer = 0xFFEADDFF.toInt(),
            onPrimaryContainer = 0xFF21005D.toInt(),
            secondary = 0xFF03DAC6.toInt(),
            onSecondary = 0xFF000000.toInt(),
            tertiary = 0xFF7D5260.toInt(),
            onTertiary = 0xFFFFFFFF.toInt()
        )

        val PresetColors = listOf(
            Default,
            AppColorScheme(
                id = "blue",
                name = "Синий",
                primary = 0xFF1E88E5.toInt(),
                onPrimary = 0xFFFFFFFF.toInt(),
                primaryContainer = 0xFFD6E3FF.toInt(),
                onPrimaryContainer = 0xFF001F3F.toInt(),
                secondary = 0xFF64B5F6.toInt(),
                onSecondary = 0xFF000000.toInt(),
                tertiary = 0xFF7D5260.toInt(),
                onTertiary = 0xFFFFFFFF.toInt()
            ),
            AppColorScheme(
                id = "red",
                name = "Красный",
                primary = 0xFFE53935.toInt(),
                onPrimary = 0xFFFFFFFF.toInt(),
                primaryContainer = 0xFFFFD6D6.toInt(),
                onPrimaryContainer = 0xFF4A0000.toInt(),
                secondary = 0xFFEF5350.toInt(),
                onSecondary = 0xFF000000.toInt(),
                tertiary = 0xFF7D5260.toInt(),
                onTertiary = 0xFFFFFFFF.toInt()
            ),
            AppColorScheme(
                id = "green",
                name = "Зелёный",
                primary = 0xFF43A047.toInt(),
                onPrimary = 0xFFFFFFFF.toInt(),
                primaryContainer = 0xFFC8E6C9.toInt(),
                onPrimaryContainer = 0xFF003300.toInt(),
                secondary = 0xFF66BB6A.toInt(),
                onSecondary = 0xFF000000.toInt(),
                tertiary = 0xFF7D5260.toInt(),
                onTertiary = 0xFFFFFFFF.toInt()
            ),
            AppColorScheme(
                id = "orange",
                name = "Оранжевый",
                primary = 0xFFFB8C00.toInt(),
                onPrimary = 0xFFFFFFFF.toInt(),
                primaryContainer = 0xFFFFE0B2.toInt(),
                onPrimaryContainer = 0xFF3E1A00.toInt(),
                secondary = 0xFFFFA726.toInt(),
                onSecondary = 0xFF000000.toInt(),
                tertiary = 0xFF7D5260.toInt(),
                onTertiary = 0xFFFFFFFF.toInt()
            ),
            AppColorScheme(
                id = "purple",
                name = "Фиолетовый",
                primary = 0xFF8E24AA.toInt(),
                onPrimary = 0xFFFFFFFF.toInt(),
                primaryContainer = 0xFFF3E5F5.toInt(),
                onPrimaryContainer = 0xFF2A0033.toInt(),
                secondary = 0xFFCE93D8.toInt(),
                onSecondary = 0xFF000000.toInt(),
                tertiary = 0xFF7D5260.toInt(),
                onTertiary = 0xFFFFFFFF.toInt()
            )
        )
    }
}