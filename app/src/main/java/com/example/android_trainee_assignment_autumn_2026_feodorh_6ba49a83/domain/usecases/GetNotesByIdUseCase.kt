package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases

import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model.Note
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.repository.NoteRepository
import javax.inject.Inject

class GetNotesByIdUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: Long): Note? = repository.getNoteById(id)
}