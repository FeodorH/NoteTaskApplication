package com.example.note_task_app.domain.usecases

import com.example.note_task_app.domain.repository.TaskRepository
import javax.inject.Inject

class UpdateTaskStatusUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(id: Long, isFinished: Boolean) {
        repository.updateTaskStatus(id, isFinished)
    }
}