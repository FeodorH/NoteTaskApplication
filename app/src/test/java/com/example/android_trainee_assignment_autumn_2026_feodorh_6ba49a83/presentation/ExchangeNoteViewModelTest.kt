package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation

import androidx.lifecycle.SavedStateHandle
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model.Note
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.service.VoiceInputService
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.CreateNoteUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.GetNoteByIdUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.notes.ExchangeNoteViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ExchangeNoteViewModelTest {

    private lateinit var viewModel: ExchangeNoteViewModel
    private val getNoteById: GetNoteByIdUseCase = mockk()
    private val createNote: CreateNoteUseCase = mockk()
    private val voiceInputService: VoiceInputService = mockk()
    private val savedStateHandle = SavedStateHandle()

    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        savedStateHandle.set("noteId", 0L) // новая заметка

        // Заглушки для UseCase
        coEvery { getNoteById(any()) } returns null
        coEvery { createNote(any()) } returns Unit

        // Заглушки для VoiceInputService
        coEvery { voiceInputService.startListening() } returns emptyFlow()
        coEvery { voiceInputService.stopListening() } returns Unit

        viewModel = ExchangeNoteViewModel(
            getNoteById = getNoteById,
            createNote = createNote,
            voiceInputService = voiceInputService,
            savedStateHandle = savedStateHandle
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `saveNote with empty title should set error message`() = runTest(testDispatcher) {
        // given
        viewModel.updateTitle("")

        // when
        viewModel.saveNote()
        advanceUntilIdle()

        // then
        val state = viewModel.state.value
        assertEquals("Заголовок не может быть пустым", state.errorMessage)
        coVerify(exactly = 0) { createNote(any()) }
    }

    @Test
    fun `saveNote with valid title should call createNote and set isSaved`() = runTest(testDispatcher) {
        // given
        viewModel.updateTitle("My Note")
        viewModel.updateContent("Some content")

        // when
        viewModel.saveNote()
        advanceUntilIdle()

        // then
        val state = viewModel.state.value
        assertTrue(state.isSaved)
        coVerify { createNote(any()) }
    }

    @Test
    fun `loadNote should populate state when note exists`() = runTest(testDispatcher) {
        // given
        val existingNote = Note(id = 1L, title = "Existing", content = "Content")
        savedStateHandle.set("noteId", 1L) // меняем ID на существующий
        coEvery { getNoteById(1L) } returns existingNote

        // Пересоздаём ViewModel с новым ID
        viewModel = ExchangeNoteViewModel(
            getNoteById = getNoteById,
            createNote = createNote,
            voiceInputService = voiceInputService,
            savedStateHandle = savedStateHandle
        )
        advanceUntilIdle()

        // then
        val state = viewModel.state.value
        assertEquals("Existing", state.title)
        assertEquals("Content", state.content)
        assertEquals(false, state.isLoading)
        coVerify { getNoteById(1L) }
    }
}