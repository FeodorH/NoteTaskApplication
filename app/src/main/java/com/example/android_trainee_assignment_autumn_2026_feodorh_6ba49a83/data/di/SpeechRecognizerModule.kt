package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.di

import android.content.Context
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.service.AndroidVoiceInputService
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.service.VoiceInputServiceFactory
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.service.VoskVoiceInputService
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.service.VoiceInputService
import dagger.Binds
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
