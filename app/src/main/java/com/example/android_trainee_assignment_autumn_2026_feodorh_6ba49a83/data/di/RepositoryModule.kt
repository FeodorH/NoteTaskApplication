package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.di

import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.repository.NoteRepositoryImpl
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.repository.NoteRepository
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
}