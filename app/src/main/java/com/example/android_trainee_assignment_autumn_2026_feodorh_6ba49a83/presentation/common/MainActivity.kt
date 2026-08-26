package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.common

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.common.navigation.AddNoteScreen
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.common.navigation.NotesScreen
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.common.navigation.SettingsScreen
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.common.navigation.TaskScreen
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.notes.AddNote
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
                    composable<AddNoteScreen>{
                        AddNote(navController = navController)
                    }
                }
            }
        }
    }
}
