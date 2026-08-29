package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.di

import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.BuildConfig
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.network.gigachat.api.GigaChatApi
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.network.gigachat.service.GigaChatServiceImpl
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.service.GigaChatService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

@Module
@InstallIn(SingletonComponent::class)
object GigaChatModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        // TODO Временно доверяем всем сертификатам (для тестирования) - для прода заменить
        val trustAllCerts = arrayOf<X509TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(
                p0: Array<out java.security.cert.X509Certificate?>?,
                p1: String?
            ) {
            }

            override fun checkServerTrusted(
                p0: Array<out java.security.cert.X509Certificate?>?,
                p1: String?
            ) {
            }

            override fun getAcceptedIssuers(): Array<out java.security.cert.X509Certificate?> =
                arrayOf()
        })
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())
        val sslSocketFactory = sslContext.socketFactory

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .sslSocketFactory(sslSocketFactory, trustAllCerts[0])
            .hostnameVerifier { _, _ -> true }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    /*@Provides
    @Singleton
    fun provideGigaChatOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }*/

    @Provides
    @Singleton
    fun provideGigaChatApi(moshi: Moshi, okHttpClient: OkHttpClient): GigaChatApi {
        return Retrofit.Builder()
            .baseUrl("https://api.giga.chat/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GigaChatApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGigaChatService(api: GigaChatApi): GigaChatService {
        return GigaChatServiceImpl(api)
    }
}