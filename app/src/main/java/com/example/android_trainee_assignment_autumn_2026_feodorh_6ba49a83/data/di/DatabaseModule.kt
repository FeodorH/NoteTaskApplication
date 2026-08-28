package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.di


import android.content.Context
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.local.dao.NoteDAO
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.local.dao.TaskDAO
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.local.database.AppDatabase
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