package com.example.note_task_app.domain.usecases

import com.example.note_task_app.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteTask(id)
    }
}