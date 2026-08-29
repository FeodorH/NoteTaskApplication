package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.network.gigachat.api

import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.network.gigachat.dto.BalanceResponse
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.network.gigachat.dto.ChatRequest
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.network.gigachat.dto.ChatResponse
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.network.gigachat.dto.ModelsResponse
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.network.gigachat.dto.TokenResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url
import java.util.UUID

interface GigaChatApi {

    // Метод получения токена – полный URL передаётся как параметр
    @POST
    @FormUrlEncoded
    suspend fun getToken(
        @Url url: String = "https://ngw.devices.sberbank.ru:9443/api/v2/oauth",
        @Header("Content-Type") contentType: String = "application/x-www-form-urlencoded",
        @Header("Accept") accept: String = "application/json",
        @Header("RqUID") rqUid: String = UUID.randomUUID().toString(),
        @Header("Authorization") authorization: String,
        @Field("scope") scope: String = "GIGACHAT_API_PERS"
    ): TokenResponse

    // Остальные методы используют относительный путь, базовый URL будет https://api.giga.chat/
    @GET("v1/models")
    suspend fun getModels(
        @Header("Accept") accept: String = "application/json",
        @Header("Authorization") authorization: String
    ): ModelsResponse

    @GET("v1/balance")
    suspend fun getBalance(
        @Header("Accept") accept: String = "application/json",
        @Header("Authorization") authorization: String
    ): BalanceResponse

    @POST("v1/chat/completions")
    suspend fun generateTask(
        @Header("Content-Type") contentType: String = "application/json",
        @Header("Accept") accept: String = "application/json",
        @Header("Authorization") authorization: String,
        @Body request: ChatRequest
    ): ChatResponse
}