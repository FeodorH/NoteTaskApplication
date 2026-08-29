package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.common.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavigationItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    object Notes : BottomNavigationItem(
        route = Routes.NOTES.route,
        icon = Icons.Default.Note,
        label = "Заметки"
    )

    object Tasks : BottomNavigationItem(
        route = Routes.TASKS.route,
        icon = Icons.Default.Checklist,
        label = "Задачи"
    )

    object Settings : BottomNavigationItem(
        route = Routes.SETTINGS.route,
        icon = Icons.Default.Settings,
        label = "Настройки"
    )
}