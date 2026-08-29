package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.notes

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model.Note
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.service.VoiceEvent
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.service.VoiceInputService
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.CreateNoteUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.GetNoteByIdUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.notes.models.ExchangeNoteUiState
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.notes.models.VoiceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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
    private val voiceInputService: VoiceInputService,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Получаем noteId из аргументов навигации (0 = новая заметка)
    internal val noteId: Long = savedStateHandle.get<Long>("noteId") ?: 0L

    private val _state = MutableStateFlow(ExchangeNoteUiState())
    open val state: StateFlow<ExchangeNoteUiState> = _state.asStateFlow()

    // Джоба голосового ввода
    private var voiceJob: Job? = null

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

    fun startVoiceInput() {
        if (voiceJob?.isActive == true) return

        _state.update { it.copy(voiceState = VoiceState.RECORDING) }

        voiceJob = viewModelScope.launch {
            voiceInputService.startListening().collect { event ->
                when (event) {
                    is VoiceEvent.Ready -> {
                        // можно не обновлять состояние
                    }

                    is VoiceEvent.Listening -> {
                        _state.update { it.copy(voiceState = VoiceState.RECORDING) }
                    }

                    is VoiceEvent.PartialResult -> {
                        // можно показать промежуточный текст, если нужно
                    }

                    is VoiceEvent.FinalResult -> {
                        // Добавляем распознанный текст в поле содержимого
                        val currentContent = _state.value.content
                        val newContent = if (currentContent.isBlank()) {
                            event.text
                        } else {
                            "$currentContent ${event.text}"//Реализация с пробелом
                        }
                        _state.update {
                            it.copy(
                                content = newContent,
                                voiceState = VoiceState.IDLE
                            )
                        }
                    }

                    is VoiceEvent.Error -> {
                        _state.update {
                            it.copy(
                                voiceState = VoiceState.IDLE,
                                errorMessage = event.message
                            )
                        }
                    }

                    is VoiceEvent.Cancelled -> {
                        _state.update { it.copy(voiceState = VoiceState.IDLE) }
                    }
                }
            }
        }
    }

    fun cancelVoiceInput() {
        voiceInputService.stopListening()
        voiceJob?.cancel()
        voiceJob = null
        _state.update { it.copy(voiceState = VoiceState.IDLE) }
    }

    override fun onCleared() {
        super.onCleared()
        voiceInputService.stopListening()
        voiceJob?.cancel()
    }
}
