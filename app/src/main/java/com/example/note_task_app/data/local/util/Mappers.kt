package com.example.note_task_app.data.local.util

import com.example.note_task_app.data.local.entities.NoteEntity
import com.example.note_task_app.data.local.entities.TaskEntity
import com.example.note_task_app.domain.model.Note
import com.example.note_task_app.domain.model.Task

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