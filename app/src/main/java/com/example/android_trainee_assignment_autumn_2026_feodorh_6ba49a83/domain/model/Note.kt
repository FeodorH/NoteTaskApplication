package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model

data class Note(
    val id: Long = 0,
    val title: String,
    val content: String? = null,
    val imageUri: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)