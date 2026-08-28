package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.tasks.models

import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model.Task

sealed class TasksUiState {
    object Loading : TasksUiState()
    data class Success(
        val tasks: List<Task> = emptyList(),
        val searchQuery: String = "",
        val filter: TaskFilter = TaskFilter.ALL,
        val isAddingMode: Boolean = false,
        val newTaskTitle: String = "",
        val errorMessage: String? = null
    ) : TasksUiState()
    data class Error(val message: String) : TasksUiState()
}

enum class TaskFilter { ALL, ACTIVE, COMPLETED }