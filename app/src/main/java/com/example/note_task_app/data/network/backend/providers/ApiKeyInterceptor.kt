package com.example.note_task_app.data.network.backend.providers

import com.example.note_task_app.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("X-API-Key", BuildConfig.NT_API_KEY)
            .build()
        return chain.proceed(request)
    }
}