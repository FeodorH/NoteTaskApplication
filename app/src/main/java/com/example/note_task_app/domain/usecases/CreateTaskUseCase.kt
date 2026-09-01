package com.example.note_task_app.domain.usecases

import com.example.note_task_app.domain.model.Task
import com.example.note_task_app.domain.repository.TaskRepository
import javax.inject.Inject

class CreateTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: Task) = repository.saveTask(task)
}