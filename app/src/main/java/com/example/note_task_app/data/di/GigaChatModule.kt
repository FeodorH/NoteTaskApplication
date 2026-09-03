package com.example.note_task_app.data.di

import android.content.Context
import com.example.note_task_app.BuildConfig
import com.example.note_task_app.R
import com.example.note_task_app.data.network.gigachat.api.GigaChatApi
import com.example.note_task_app.data.network.gigachat.service.GigaChatServiceImpl
import com.example.note_task_app.domain.service.GigaChatService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
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
    @Singleton // с подгрузкой сертификатов
    fun provideGigaChatOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType()).apply {
            load(null, null)
            // Загружаем корневой сертификат
            val rootCert = loadCertificateFromRaw(context, R.raw.root_ca)
            setCertificateEntry("root_ca", rootCert)
            // Загружаем промежуточный сертификат
            val subCert = loadCertificateFromRaw(context, R.raw.sub_ca)
            setCertificateEntry("sub_ca", subCert)
            // Загружаем серверный сертификат (для ngw.devices.sberbank.ru)
            val serverCert = loadCertificateFromRaw(context, R.raw.server_cert)
            setCertificateEntry("server_cert", serverCert)
        }

        val trustManagerFactory = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm()
        ).apply {
            init(keyStore)
        }
        val trustManager = trustManagerFactory.trustManagers.first() as X509TrustManager

        val sslContext = SSLContext.getInstance("TLS").apply {
            init(null, arrayOf(trustManager), null)
        }

        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .sslSocketFactory(sslContext.socketFactory, trustManager)
            .hostnameVerifier { hostname, _ ->
                // Разрешаем оба домена, используемые GigaChat
                hostname == "ngw.devices.sberbank.ru" ||
                        hostname == "api.giga.chat" ||
                        hostname.endsWith(".giga.chat")
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private fun loadCertificateFromRaw(context: Context, rawResId: Int): X509Certificate {
        context.resources.openRawResource(rawResId).use { inputStream ->
            val certificateFactory = CertificateFactory.getInstance("X.509")
            return certificateFactory.generateCertificate(inputStream) as X509Certificate
        }
    }

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