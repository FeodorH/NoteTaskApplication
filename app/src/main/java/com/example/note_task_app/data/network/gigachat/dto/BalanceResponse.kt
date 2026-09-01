package com.example.note_task_app.data.network.gigachat.dto

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