package com.example.note_task_app.domain.model

import com.example.note_task_app.ui.theme.*

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
            primary = violetPrimary,
            onPrimary = violetOnPrimary,
            primaryContainer = violetPrimaryContainer,
            onPrimaryContainer = violetOnPrimaryContainer,
            secondary = violetSecondary,
            onSecondary = violetOnSecondary,
            tertiary = violetTertiary,
            onTertiary = violetOnTertiary
        )

        val PresetColors = listOf(
            Default,
            AppColorScheme(
                id = "blue",
                name = "Синий",
                primary = bluePrimary,
                onPrimary = blueOnPrimary,
                primaryContainer = bluePrimaryContainer,
                onPrimaryContainer = blueOnPrimaryContainer,
                secondary = blueSecondary,
                onSecondary = blueOnSecondary,
                tertiary = blueTertiary,
                onTertiary = blueOnTertiary
            ),
            AppColorScheme(
                id = "red",
                name = "Красный",
                primary = redPrimary,
                onPrimary = redOnPrimary,
                primaryContainer = redPrimaryContainer,
                onPrimaryContainer = redOnPrimaryContainer,
                secondary = redSecondary,
                onSecondary = redOnSecondary,
                tertiary = redTertiary,
                onTertiary = redOnTertiary
            ),
            AppColorScheme(
                id = "green",
                name = "Зелёный",
                primary = greenPrimary,
                onPrimary = greenOnPrimary,
                primaryContainer = greenPrimaryContainer,
                onPrimaryContainer = greenOnPrimaryContainer,
                secondary = greenSecondary,
                onSecondary = greenOnSecondary,
                tertiary = greenTertiary,
                onTertiary = greenOnTertiary
            ),
            AppColorScheme(
                id = "orange",
                name = "Оранжевый",
                primary = orangePrimary,
                onPrimary = orangeOnPrimary,
                primaryContainer = orangePrimaryContainer,
                onPrimaryContainer = orangeOnPrimaryContainer,
                secondary = orangeSecondary,
                onSecondary = orangeOnSecondary,
                tertiary = orangeTertiary,
                onTertiary = orangeOnTertiary
            ),
            AppColorScheme(
                id = "violet",
                name = "Фиолетовый",
                primary = violetPrimary,
                onPrimary = violetOnPrimary,
                primaryContainer = violetPrimaryContainer,
                onPrimaryContainer = violetOnPrimaryContainer,
                secondary = violetSecondary,
                onSecondary = violetOnSecondary,
                tertiary = violetTertiary,
                onTertiary = violetOnTertiary
            )
        )
    }
}