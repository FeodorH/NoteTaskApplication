package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = -1,// Если осталось -1 значит где-то ошибка генерации
    val title: String,
    val content: String? = null,
    val imageUri: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)