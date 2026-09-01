package com.example.note_task_app.data

import com.example.note_task_app.data.local.dao.NoteDAO
import com.example.note_task_app.data.local.entities.NoteEntity
import com.example.note_task_app.data.repository.NoteRepositoryImpl
import com.example.note_task_app.domain.model.Note
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class NoteRepositoryImplTest {

    private val noteDao: NoteDAO = mockk()
    private val repository = NoteRepositoryImpl(noteDao)

    @Test
    fun `getNotesFlow should return list of notes from DAO`() = runTest {
        // given
        val entities = listOf(NoteEntity(id = 1, title = "Test", content = "Content"))
        coEvery { noteDao.getAllNotes() } returns flowOf(entities)

        // when
        val result = repository.getNotesFlow()

        // then
        result.collect { notes ->
            assertEquals(1, notes.size)
            assertEquals("Test", notes.first().title)
        }
    }

    @Test
    fun `saveNote should call insertNote on DAO`() = runTest {
        // given
        val note = Note(id = 0, title = "New", content = "Content")
        coEvery { noteDao.insertNote(any()) } returns 0

        // when
        repository.saveNote(note)

        // then
        coVerify { noteDao.insertNote(any()) }
    }

    @Test
    fun `deleteNote should call deleteNote on DAO`() = runTest {
        // given
        val id = 1L
        coEvery { noteDao.deleteNote(id) } returns 0

        // when
        repository.deleteNote(id)

        // then
        coVerify { noteDao.deleteNote(id) }
    }
}
