package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.local.util

import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.local.entities.NoteEntity
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model.Note

fun NoteEntity.toDomain(): Note = Note(
    id = id,
    title = title,
    content = content,
    imageUri = imageUri,
    createdAt = createdAt
)

fun Note.toEntity(): NoteEntity = NoteEntity(
    id = id,
    title = title,
    content = content,
    imageUri = imageUri,
    createdAt = createdAt
)