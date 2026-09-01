package com.example.note_task_app.domain.usecases

import com.example.note_task_app.domain.repository.NoteRepository
import javax.inject.Inject

class DeleteNoteByIdUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: Long) = repository.deleteNote(id)
}