package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.network.gigachat.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BalanceResponse(
    val balance: List<BalanceItem>
)

@JsonClass(generateAdapter = true)
data class BalanceItem(
    val usage: String,
    val value: Double
)