package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.local.util

import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.local.entities.NoteEntity
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.local.entities.TaskEntity
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model.Note
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model.Task

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

fun TaskEntity.toDomain() = Task(
    id,
    title,
    isCompleted,
    createdAt
)

fun Task.toEntity() = TaskEntity(
    id,
    title,
    isCompleted,
    createdAt
)