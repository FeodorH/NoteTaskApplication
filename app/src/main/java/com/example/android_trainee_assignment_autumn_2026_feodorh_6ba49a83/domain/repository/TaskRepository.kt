package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.repository

import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getTasksFlow(): Flow<List<Task>>
    suspend fun saveTask(task: Task)
    suspend fun deleteTask(id: Long)
    suspend fun updateTaskStatus(id: Long, isCompleted: Boolean)
}