package com.example.note_task_app.data.network.gigachat.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TokenResponse(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "expires_at") val expiresAt: Long? = null,
    val scope: String? = null
)