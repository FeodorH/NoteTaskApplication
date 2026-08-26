package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.common

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.common.navigation.ExchangeNoteScreen
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.common.navigation.NotesScreen
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.common.navigation.SettingsScreen
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.common.navigation.TaskScreen
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.notes.ExchangeNote
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.notes.Notes
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.settings.Settings
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.tasks.Tasks
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.ui.theme.Androidtraineeassignmentautumn2026feodorh6ba49a83Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Androidtraineeassignmentautumn2026feodorh6ba49a83Theme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = NotesScreen
                ) {
                    composable<NotesScreen> {
                        Notes(navController = navController)
                    }
                    composable<TaskScreen> {
                        Tasks(navController = navController)
                    }
                    composable<SettingsScreen> {
                        Settings(navController = navController)
                    }
                    composable<ExchangeNoteScreen>{
                        ExchangeNote(navController = navController)
                    }
                }
            }
        }
    }
}
