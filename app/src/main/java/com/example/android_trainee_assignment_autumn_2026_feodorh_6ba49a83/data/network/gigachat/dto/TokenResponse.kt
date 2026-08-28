package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.network.gigachat.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TokenResponse(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "expires_at") val expiresAt: Long? = null,
    val scope: String? = null
)