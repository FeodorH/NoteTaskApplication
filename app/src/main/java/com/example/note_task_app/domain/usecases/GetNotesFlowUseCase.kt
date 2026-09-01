package com.example.note_task_app.domain.usecases

import com.example.note_task_app.domain.model.Note
import com.example.note_task_app.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotesFlowUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    operator fun invoke(): Flow<List<Note>> = repository.getNotesFlow()
}