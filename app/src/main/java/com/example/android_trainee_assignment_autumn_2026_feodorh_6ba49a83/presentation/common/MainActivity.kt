package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.common

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.common.navigation.Routes
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.common.theme.AppTheme
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.notes.ExchangeNoteScreen
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.notes.NotesScreen
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.settings.SettingsScreen
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.settings.SettingsViewModel
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.tasks.TasksScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val uiState by settingsViewModel.settingsUiState.collectAsState()

            AppTheme(
                themeMode = uiState.themeMode,
                colorScheme = uiState.colorScheme
            ) {
                Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
                    NavHost(
                        navController = navController,
                        startDestination = Routes.NOTES.route,
                        modifier = Modifier.weight(1f)
                    ) {
                        composable(Routes.NOTES.route) {
                            NotesScreen(navController = navController)
                        }
                        composable(Routes.TASKS.route) {
                            TasksScreen(navController = navController)
                        }
                        composable(Routes.SETTINGS.route) {
                            SettingsScreen(
                                navController = navController,
                                settingsViewModel
                            )
                        }
                        composable(
                            route = Routes.EDIT_NOTE.route,
                            arguments = listOf(navArgument("noteId") { type = NavType.LongType })
                        ) {
                            ExchangeNoteScreen(navController = navController)
                        }
                    }
                    BottomNavigationPanel(navController = navController)
                }
            }
        }
    }
}
