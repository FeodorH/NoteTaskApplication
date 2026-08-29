package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation

import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model.Note
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.*
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.notes.NotesViewModel
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.notes.models.SortOrder
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class NotesViewModelTest {

    private lateinit var viewModel: NotesViewModel
    private val getNotesFlow: GetNotesFlowUseCase = mockk()
    private val getNoteById: GetNoteByIdUseCase = mockk()
    private val createNote: CreateNoteUseCase = mockk()
    private val deleteNoteById: DeleteNoteByIdUseCase = mockk()
    private val deleteAllNotes: DeleteAllNotesUseCase = mockk()

    private val notesFlow = MutableStateFlow<List<Note>>(emptyList())
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        val notes = listOf(
            Note(id = 1, title = "A note", content = "aaa", createdAt = 1000),
            Note(id = 2, title = "B note", content = "bbb", createdAt = 2000),
            Note(id = 3, title = "C note", content = "ccc", createdAt = 3000)
        )
        notesFlow.value = notes
        coEvery { getNotesFlow() } returns notesFlow
        coEvery { getNoteById(any()) } returns null
        coEvery { createNote(any()) } returns Unit
        coEvery { deleteNoteById(any()) } returns Unit
        coEvery { deleteAllNotes() } returns Unit

        viewModel = NotesViewModel(
            getNotesFlow = getNotesFlow,
            getNoteById = getNoteById,
            createNote = createNote,
            deleteNoteById = deleteNoteById,
            deleteAllNotes = deleteAllNotes
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `updateSearchQuery should filter notes by title`() = runTest(testDispatcher) {
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.updateSearchQuery("B")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(1, state.notes.size)
        assertEquals("B note", state.notes.first().title)
    }

    @Test
    fun `toggleSortOrder should change sort order and reorder notes`() = runTest(testDispatcher) {
        testDispatcher.scheduler.advanceUntilIdle()

        val initial = viewModel.state.value
        assertEquals(3, initial.notes.size)
        assertEquals("C note", initial.notes.first().title)

        viewModel.toggleSortOrder()
        testDispatcher.scheduler.advanceUntilIdle()

        val newState = viewModel.state.value
        assertEquals("A note", newState.notes.first().title)
    }
}
