package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.notes.models

import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model.Note

data class NotesUIState (
    val notes: List<Note> = emptyList(),
    val searchQuery: String = "",
    val sortOrder: SortOrder = SortOrder.NEWEST,
    val isDeleteMode: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

enum class SortOrder { NEWEST, OLDEST }