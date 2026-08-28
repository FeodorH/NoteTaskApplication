package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.local.dao.NoteDAO
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.local.dao.TaskDAO
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.local.entities.NoteEntity
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.local.entities.TaskEntity
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.local.migration.MIGRATION_1_TO_2
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.local.util.Converters


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