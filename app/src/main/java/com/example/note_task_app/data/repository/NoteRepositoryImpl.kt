package com.example.note_task_app.data.repository

import com.example.note_task_app.data.local.dao.NoteDAO
import com.example.note_task_app.data.local.util.toDomain
import com.example.note_task_app.data.local.util.toEntity
import com.example.note_task_app.domain.model.Note
import com.example.note_task_app.domain.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDAO
) : NoteRepository {

    override fun getNotesFlow(): Flow<List<Note>> {
        return noteDao.getAllNotes()
            .map { entities -> entities.map { it.toDomain() } }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun getNoteById(id: Long): Note? = withContext(Dispatchers.IO) {
        noteDao.getNoteById(id)?.toDomain()
    }

    override suspend fun saveNote(note: Note) = withContext(Dispatchers.IO) {
        noteDao.insertNote(note.toEntity())
        Unit
    }

    override suspend fun deleteNote(id: Long) = withContext(Dispatchers.IO) {
        noteDao.deleteNote(id)
        Unit
    }

    override suspend fun deleteAllNotes() = withContext(Dispatchers.IO) {
        noteDao.deleteAll()
        Unit
    }
}