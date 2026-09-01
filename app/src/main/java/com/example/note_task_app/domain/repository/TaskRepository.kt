package com.example.note_task_app.domain.repository

import com.example.note_task_app.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getTasksFlow(): Flow<List<Task>>
    suspend fun saveTask(task: Task)
    suspend fun deleteTask(id: Long)
    suspend fun updateTaskStatus(id: Long, isCompleted: Boolean)
}