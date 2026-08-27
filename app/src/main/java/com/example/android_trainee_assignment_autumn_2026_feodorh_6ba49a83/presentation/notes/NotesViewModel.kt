package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model.Note
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.CreateNoteUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.DeleteAllNotesUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.DeleteNoteByIdUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.GetNoteByIdUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.GetNotesFlowUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.notes.models.NotesUiState
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.notes.models.SortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    val getNotesFlow : GetNotesFlowUseCase,
    val getNoteById : GetNoteByIdUseCase,
    val createNote : CreateNoteUseCase,
    val deleteNoteById : DeleteNoteByIdUseCase,
    val deleteAllNotes : DeleteAllNotesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(NotesUiState())
    val state: StateFlow<NotesUiState> = _state.asStateFlow()

    // Внутренние флоу для быстрой фильтрации и поиска
    private val searchQueryFlow = MutableStateFlow("")
    private val sortOrderFlow = MutableStateFlow(SortOrder.NEWEST)
    // Связь с View
    val searchQuery: StateFlow<String> = searchQueryFlow.asStateFlow()

    init {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            combine(
                getNotesFlow(),
                searchQueryFlow,
                sortOrderFlow
            ) { notes, query, sortOrder ->
                applyFiltersAndSort(notes, query, sortOrder)
            }.collect { filteredNotes ->
                _state.update { it.copy(notes = filteredNotes, isLoading = false) }
            }
        }
    }

    fun loadNotes() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getNotesFlow().collect { list ->
                _state.update { it.copy(notes = list, isLoading = false) }
            }
        }
    }

    fun updateSearchQuery(query: String) {
        searchQueryFlow.value = query
    }

    fun toggleSortOrder() {
        val current = sortOrderFlow.value
        sortOrderFlow.value = if (current == SortOrder.NEWEST) SortOrder.OLDEST else SortOrder.NEWEST
    }

    fun toggleDeleteMode() {
        _state.update { it.copy(isDeleteMode = !it.isDeleteMode) }
    }

    fun deleteNote(noteId: Long) {
        viewModelScope.launch {
            deleteNoteById(noteId)
            _state.update { state ->
                state.copy(notes = state.notes.filter { it.id != noteId })
            }
        }
    }

    private fun applyFiltersAndSort(
        notes: List<Note>,
        query: String,
        sortOrder: SortOrder
    ): List<Note> {
        val filtered = if (query.isBlank()) {
            notes
        } else {
            notes.filter { it.title.contains(query, ignoreCase = true) }
        }
        return when (sortOrder) {
            SortOrder.NEWEST -> filtered.sortedByDescending { it.createdAt }
            SortOrder.OLDEST -> filtered.sortedBy { it.createdAt }
        }
    }
}