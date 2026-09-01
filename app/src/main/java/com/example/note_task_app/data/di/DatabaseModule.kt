package com.example.note_task_app.data.di


import android.content.Context
import com.example.note_task_app.data.local.dao.NoteDAO
import com.example.note_task_app.data.local.dao.TaskDAO
import com.example.note_task_app.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        AppDatabase.getInstance(context)

    @Provides
    fun provideNoteDao(db: AppDatabase): NoteDAO = db.noteDao()

    @Provides
    fun provideTaskDao(db: AppDatabase): TaskDAO = db.taskDao()
}