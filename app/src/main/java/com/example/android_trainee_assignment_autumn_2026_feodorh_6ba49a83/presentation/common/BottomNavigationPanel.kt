package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.common

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.common.navigation.BottomNavigationItem
import java.lang.reflect.Modifier

@Composable
fun BottomNavigationPanel(navController: NavController) {
    val items = listOf(
        BottomNavigationItem.Notes,
        BottomNavigationItem.Tasks,
        BottomNavigationItem.Settings
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        // Чтобы избежать накопления бекстека при многократных кликах
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}