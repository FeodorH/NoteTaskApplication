package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.local.dao.NoteDAO
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.local.entities.NoteEntity
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.local.util.Converters


@Database(
    entities = [NoteEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}