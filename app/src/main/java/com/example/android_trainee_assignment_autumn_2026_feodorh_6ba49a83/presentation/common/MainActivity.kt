package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.common

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.common.navigation.Routes
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.notes.ExchangeNote
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.notes.Notes
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.settings.Settings
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.ui.theme.Androidtraineeassignmentautumn2026feodorh6ba49a83Theme
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.tasks.Tasks
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            Androidtraineeassignmentautumn2026feodorh6ba49a83Theme {
                Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
                    NavHost(
                        navController = navController,
                        startDestination = Routes.NOTES.route,
                        modifier = Modifier.weight(1f)
                    ) {
                        composable(Routes.NOTES.route) {
                            Notes(navController = navController)
                        }
                        composable(Routes.TASKS.route) {
                            Tasks(navController = navController)
                        }
                        composable(Routes.SETTINGS.route) {
                            Settings(navController = navController)
                        }
                        composable(
                            route = Routes.EDIT_NOTE.route,
                            arguments = listOf(navArgument("noteId") { type = NavType.LongType })
                        ) {
                            ExchangeNote(navController = navController)
                        }
                    }
                    BottomNavigationPanel(navController = navController)
                }
            }
        }
    }
}
