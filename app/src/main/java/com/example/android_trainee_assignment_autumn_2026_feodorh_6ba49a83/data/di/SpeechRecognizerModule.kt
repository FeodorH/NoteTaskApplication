package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.di

import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.service.AndroidVoiceInputService
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.service.VoiceInputService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SpeechRecognizerModule {
    @Binds
    abstract fun bindVoiceInputService(impl: AndroidVoiceInputService): VoiceInputService
}