package com.example.note_task_app.data.network.gigachat.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ModelsResponse(
    val data: List<Model>
)

@JsonClass(generateAdapter = true)
data class Model(
    val id: String,
    val `object`: String = "model",  // поле "object" может быть зарезервировано, используем обратные кавычки
    val created: Long,
    @Json(name = "owned_by") val ownedBy: String
)