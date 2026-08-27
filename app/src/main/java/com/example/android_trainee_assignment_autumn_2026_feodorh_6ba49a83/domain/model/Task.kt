package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model

data class Task(
    val id: Long = 0,
    val title: String,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)