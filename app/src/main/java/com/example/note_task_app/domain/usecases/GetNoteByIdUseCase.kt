package com.example.note_task_app.domain.usecases

import com.example.note_task_app.domain.model.Note
import com.example.note_task_app.domain.repository.NoteRepository
import javax.inject.Inject

class GetNoteByIdUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: Long): Note? = repository.getNoteById(id)
}