package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases

import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.repository.NoteRepository
import javax.inject.Inject

class DeleteNoteByIdUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: Long) = repository.deleteNote(id)
}