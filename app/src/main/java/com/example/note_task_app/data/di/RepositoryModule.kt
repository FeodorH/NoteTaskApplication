package com.example.note_task_app.data.di

import com.example.note_task_app.data.repository.NoteRepositoryImpl
import com.example.note_task_app.data.repository.TaskRepositoryImpl
import com.example.note_task_app.domain.repository.NoteRepository
import com.example.note_task_app.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNoteRepository(impl: NoteRepositoryImpl): NoteRepository

    @Binds
    @Singleton
    abstract fun bindTaskRepository(impl: TaskRepositoryImpl): TaskRepository
}