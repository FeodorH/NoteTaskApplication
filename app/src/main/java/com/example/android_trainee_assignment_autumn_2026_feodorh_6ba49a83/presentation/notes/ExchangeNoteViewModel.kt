package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.notes

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model.Note
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.CreateNoteUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.GetNoteByIdUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.notes.models.ExchangeNoteUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class ExchangeNoteViewModel @Inject constructor(
    private val getNoteById: GetNoteByIdUseCase,
    private val createNote: CreateNoteUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Получаем noteId из аргументов навигации (0 = новая заметка)
    internal val noteId: Long = savedStateHandle.get<Long>("noteId") ?: 0L

    private val _state = MutableStateFlow(ExchangeNoteUiState())
    open val state: StateFlow<ExchangeNoteUiState> = _state.asStateFlow()

    init {
        if (noteId != 0L) {
            loadNote(noteId)
        }
    }

    private fun loadNote(id: Long) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val note = getNoteById(id)
                if (note != null) {
                    _state.update {
                        it.copy(
                            title = note.title,
                            content = note.content ?: "",
                            imageUri = note.imageUri,
                            createdAt = note.createdAt,
                            isLoading = false
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Заметка не найдена"
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Ошибка загрузки заметки"
                    )
                }
            }
        }
    }

    fun updateTitle(title: String) {
        _state.update { it.copy(title = title) }
    }

    fun updateContent(content: String) {
        _state.update { it.copy(content = content) }
    }

    fun updateImageUri(uri: String?) {
        _state.update { it.copy(imageUri = uri) }
    }

    fun removeImage() {
        _state.update { it.copy(imageUri = null) }
    }

    fun saveNote() {
        viewModelScope.launch {
            val title = _state.value.title
            if (title.isBlank()) {
                _state.update { it.copy(errorMessage = "Заголовок не может быть пустым") }
                return@launch
            }

            _state.update { it.copy(isSaving = true, errorMessage = null) }

            try {
                val note = Note(
                    id = noteId,
                    title = title,
                    content = _state.value.content,
                    imageUri = _state.value.imageUri,
                    createdAt = if (noteId == 0L) System.currentTimeMillis() else _state.value.createdAt
                )
                createNote(note)
                _state.update { it.copy(isSaving = false, isSaved = true) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = e.message ?: "Ошибка сохранения заметки"
                    )
                }
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }
}
