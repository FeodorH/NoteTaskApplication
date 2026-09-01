package com.example.note_task_app.domain.repository

import com.example.note_task_app.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getNotesFlow(): Flow<List<Note>>
    suspend fun getNoteById(id: Long): Note?
    suspend fun saveNote(note: Note)
    suspend fun deleteNote(id: Long)
    suspend fun deleteAllNotes()
}