package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.local.entities.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDAO {
    @Query("SELECT * FROM notes ORDER BY createdAt DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: Long): @JvmSuppressWildcards NoteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): @JvmSuppressWildcards Long

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteNote(id: Long): @JvmSuppressWildcards Int

    @Query("DELETE FROM notes")
    suspend fun deleteAll(): @JvmSuppressWildcards Int
}