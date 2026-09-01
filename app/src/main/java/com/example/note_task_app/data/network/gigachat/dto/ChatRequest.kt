package com.example.note_task_app.data.network.gigachat.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class ChatRequest(
    val model: String = "GigaChat-3-Ultra",
    val messages: List<Message>,
    val temperature: Double = 0.7,
    @Json(name = "max_tokens") val maxTokens: Int = 100
)

@JsonClass(generateAdapter = true)
data class Message(
    val role: String,
    val content: String
)

@JsonClass(generateAdapter = true)
data class ChatResponse(
    val choices: List<Choice>
)

@JsonClass(generateAdapter = true)
data class Choice(
    val message: Message,
    @Json(name = "finish_reason") val finishReason: String? = null,
    val index: Int = 0
)
