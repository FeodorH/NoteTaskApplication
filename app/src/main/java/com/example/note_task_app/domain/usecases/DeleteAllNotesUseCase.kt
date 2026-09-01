package com.example.note_task_app.domain.usecases

import com.example.note_task_app.domain.repository.NoteRepository
import javax.inject.Inject

class DeleteAllNotesUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke() = repository.deleteAllNotes()
}