package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.local.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.local.entities.NoteEntity
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.local.entities.TaskEntity
import kotlinx.coroutines.flow.Flow

interface TaskDAO {
    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: Long): @JvmSuppressWildcards TaskEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): @JvmSuppressWildcards Long

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTask(id: Long): @JvmSuppressWildcards Int
}