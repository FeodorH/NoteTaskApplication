package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.notes.models

data class ExchangeNoteUiState(
    val noteId : Long = 1,
    val title: String = "",
    val content: String = "",
    val imageUri: String? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)