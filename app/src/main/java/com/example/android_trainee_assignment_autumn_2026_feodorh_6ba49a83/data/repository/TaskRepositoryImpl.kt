package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.repository

import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.local.dao.TaskDAO
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.local.util.toDomain
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.local.util.toEntity
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model.Task
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val taskDAO: TaskDAO
) : TaskRepository {
    override fun getTasksFlow(): Flow<List<Task>> =
        taskDAO.getAllTasks()
            .map { entities -> entities.map { it.toDomain() } }
            .flowOn(Dispatchers.IO)


    override suspend fun saveTask(task: Task) = withContext(Dispatchers.IO){
        taskDAO.insertTask(task.toEntity())
        Unit
    }

    override suspend fun updateTaskStatus(id: Long, isCompleted: Boolean) = withContext(Dispatchers.IO) {
        taskDAO.updateTaskStatus(id, isCompleted)
        Unit
    }

    override suspend fun deleteTask(id: Long) = withContext(Dispatchers.IO) {
        taskDAO.deleteTask(id)
        Unit
    }
}