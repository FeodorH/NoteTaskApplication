package com.example.note_task_app.domain.usecases

import com.example.note_task_app.domain.model.Task
import com.example.note_task_app.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTasksFlowUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke(): Flow<List<Task>> = repository.getTasksFlow()
}