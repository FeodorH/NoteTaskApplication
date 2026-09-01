package com.example.note_task_app.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.note_task_app.data.local.dao.NoteDAO
import com.example.note_task_app.data.local.dao.TaskDAO
import com.example.note_task_app.data.local.entities.NoteEntity
import com.example.note_task_app.data.local.entities.TaskEntity
import com.example.note_task_app.data.local.migration.MIGRATION_1_TO_2


@Database(
    entities = [NoteEntity::class, TaskEntity::class],
    version = 2,
    exportSchema = true
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDAO
    abstract fun taskDao(): TaskDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).addMigrations(MIGRATION_1_TO_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}