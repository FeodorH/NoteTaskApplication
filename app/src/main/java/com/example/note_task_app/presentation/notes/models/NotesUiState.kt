package com.example.note_task_app.presentation.notes.models

import com.example.note_task_app.domain.model.Note

data class NotesUiState(
    val notes: List<Note> = emptyList(),
    val sortOrder: SortOrder = SortOrder.NEWEST,
    val isDeleteMode: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

enum class SortOrder { NEWEST, OLDEST }