package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.repository

import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getNotesFlow(): Flow<List<Note>>
    suspend fun getNoteById(id: Long): Note?
    suspend fun saveNote(note: Note)
    suspend fun deleteNote(id: Long)
    suspend fun deleteAllNotes()
}