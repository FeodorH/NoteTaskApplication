package com.example.note_task_app.data.network.backend.service

import android.util.Log
import com.example.note_task_app.data.network.backend.api.BackendApi
import com.example.note_task_app.data.network.backend.dto.ApiRequest
import com.example.note_task_app.data.network.backend.providers.ApiKeyInterceptor
import com.example.note_task_app.domain.service.BackendService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackendServiceImpl @Inject constructor(
    private val api: BackendApi
) : BackendService {

    private val _balance = MutableStateFlow<Double?>(null)
    override val balance: StateFlow<Double?> = _balance.asStateFlow()

    override suspend fun getBalance(): Double? {
        var parsedBalance: Double? = null
        try {
            val response = api.getBalance()
            parsedBalance = response.balance.toDoubleOrNull()
            _balance.value = parsedBalance
        } catch (e: Exception) {
            Log.e("gigachat","Get balance error")
        }
        return parsedBalance
    }

    override suspend fun generateTaskFromVoice(voiceText: String): String? {
        var result: String? = null
        try {
            val request = ApiRequest(prompt = voiceText)
            val response = api.sendTaskToResponse(request)

            // Проверяем статус. В вашем Spring Boot бэкенде успешный ответ возвращает "200"
            if (response.status == "200" || response.status == "OK") {
                result = response.response
            } else if(response.status == "401"){
                throw Exception("401: Authorization error!")
            } else {
                throw Exception("Something went wrong!")
            }
        } catch (e: Exception) {
            Log.e("gigachat", e.message?:"Unknown error!")
        }
        return result
    }
}