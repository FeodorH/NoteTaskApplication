package com.example.note_task_app.data.network.gigachat.service

import android.util.Log
import com.example.note_task_app.BuildConfig
import com.example.note_task_app.data.network.gigachat.api.GigaChatApi
import com.example.note_task_app.data.network.gigachat.dto.ChatRequest
import com.example.note_task_app.data.network.gigachat.dto.Message
import com.example.note_task_app.domain.service.GigaChatService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GigaChatServiceImpl @Inject constructor(
    private val api: GigaChatApi
) : GigaChatService {

    // Кешируем токен в памяти
    private var cachedToken: String? = null
    private var tokenExpiry: Long = 0L

    private val _balance = MutableStateFlow<Double?>(null)
    override val balance: StateFlow<Double?> = _balance

    private fun getBasicAuthHeader(): String {
        val credentials = "${BuildConfig.GIGACHAT_CLIENT_ID}:${BuildConfig.GIGACHAT_CLIENT_SECRET}"
        val encoded = android.util.Base64.encodeToString(
            credentials.toByteArray(Charsets.UTF_8),
            android.util.Base64.NO_WRAP
        )
        return "Basic $encoded"
    }

    private suspend fun ensureToken() {
        if (cachedToken == null || System.currentTimeMillis() > tokenExpiry) {
            val response = api.getToken(authorization = getBasicAuthHeader())
            cachedToken = response.accessToken
            // Токен живёт 30 минут, задаём таймаут на 25 минут для безопасности
            tokenExpiry = System.currentTimeMillis() + 25 * 60 * 1000
        }
    }

    override suspend fun getBalance(): Double? {
        ensureToken()
        val token = cachedToken ?: return null
        val response = api.getBalance(authorization = "Bearer $token")
        val balanceValue = response.balance.find { it.usage == "GigaChat" }?.value ?: 0.0
        _balance.emit(balanceValue)
        return balanceValue
    }

    override suspend fun generateTaskFromVoice(voiceText: String): String? {
        ensureToken()
        val token = cachedToken ?: return null

        val prompt =
            "Преобразуй следующий текст в чёткую задачу. Верни только текст задачи, без лишних слов. Текст: $voiceText"

        val request = ChatRequest(
            model = "GigaChat-3-Ultra",
            messages = listOf(
                Message(
                    role = "system",
                    content = "Ты помощник, который преобразует текст в задачу."
                ),
                Message(role = "user", content = prompt)
            )
        )

        val response = api.generateTask(
            authorization = "Bearer $token",
            request = request
        )
        val content = response.choices.firstOrNull()?.message?.content
        Log.d("GigaChat", "Generated task content: $content")
        return content
    }
}