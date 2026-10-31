package com.example.note_task_app.data.network.backend.api

import com.example.note_task_app.BuildConfig
import com.example.note_task_app.data.network.backend.dto.ApiRequest
import com.example.note_task_app.data.network.backend.dto.ApiResponse
import com.example.note_task_app.data.network.backend.dto.BalanceResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

interface BackendApi {
    @POST("v1/generate")
    suspend fun sendTaskToResponse(@Body body: ApiRequest): ApiResponse

    @GET("v1/balance")
    suspend fun getBalance(): BalanceResponse
}