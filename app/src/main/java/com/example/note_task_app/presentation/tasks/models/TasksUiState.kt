package com.example.note_task_app.presentation.tasks.models

import com.example.note_task_app.domain.model.Task

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