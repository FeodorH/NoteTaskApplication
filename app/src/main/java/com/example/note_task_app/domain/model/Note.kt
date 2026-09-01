package com.example.note_task_app.domain.model

data class Note(
    val id: Long = 0,
    val title: String,
    val content: String? = null,
    val imageUri: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)