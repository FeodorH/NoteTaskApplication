package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.tasks.models

import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model.Task

data class TasksUiState(
    val tasks: List<Task> = emptyList(),
    val searchQuery: String = "",
    val filter: TaskFilter = TaskFilter.ALL,
    val isAddingMode: Boolean = false,
    val newTaskTitle: String = "",
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val isRecording: Boolean = false
)

enum class TaskFilter { ALL, ACTIVE, COMPLETED }