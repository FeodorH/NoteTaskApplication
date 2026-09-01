package com.example.note_task_app.data.di

import android.content.Context
import com.example.note_task_app.data.service.AndroidVoiceInputService
import com.example.note_task_app.data.service.VoiceInputServiceFactory
import com.example.note_task_app.data.service.VoskVoiceInputService
import com.example.note_task_app.domain.service.VoiceInputService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SpeechRecognizerModule {
    @Provides
    @Singleton
    fun provideAndroidVoiceInputService(
        @ApplicationContext context: Context
    ): AndroidVoiceInputService = AndroidVoiceInputService(context)

    @Provides
    @Singleton
    fun provideVoskVoiceInputService(
        @ApplicationContext context: Context
    ): VoskVoiceInputService = VoskVoiceInputService(context)

    @Provides
    @Singleton
    fun provideVoiceInputService(factory: VoiceInputServiceFactory): VoiceInputService {
        return factory.getService()
    }

    @Provides
    @Singleton
    fun provideVoiceInputServiceFactory(
        @ApplicationContext context: Context,
        googleService: AndroidVoiceInputService,
        voskService: VoskVoiceInputService
    ): VoiceInputServiceFactory {
        return VoiceInputServiceFactory(context, googleService, voskService)
    }
}
