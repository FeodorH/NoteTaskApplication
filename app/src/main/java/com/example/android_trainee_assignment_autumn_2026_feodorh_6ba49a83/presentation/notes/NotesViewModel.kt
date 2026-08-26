package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model.Note
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.notes.models.NotesUiState
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.notes.models.SortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    // TODO add usecases
) : ViewModel() {

    private val _state = MutableStateFlow(NotesUiState())
    val state: StateFlow<NotesUiState> = _state.asStateFlow()

    fun loadNotes() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val notes =
            _state.update {
                it.copy(
                    notes = dummyNotes,
                    isLoading = false,
                    errorMessage = null
                )
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _state.update { it.copy(searchQuery = query) }
        // TODO здесь можно применить фильтрацию (лучше сделать через Flow комбинирование)
    }

    fun toggleSortOrder() {
        val current = _state.value.sortOrder
        val newOrder = if (current == SortOrder.NEWEST) SortOrder.OLDEST else SortOrder.NEWEST
        _state.update { it.copy(sortOrder = newOrder) }
    }

    fun toggleDeleteMode() {
        _state.update { it.copy(isDeleteMode = !it.isDeleteMode) }
    }

    fun deleteNote(noteId: Long) {
        viewModelScope.launch {
            // TODO: удалить через репозиторий
            _state.update { state ->
                state.copy(notes = state.notes.filter { it.id != noteId })
            }
        }
    }

    fun onNoteClick(noteId: Long) {
        if (_state.value.isDeleteMode) {
            return
        }
        // TODO: навигация на экран редактирования
    }
}