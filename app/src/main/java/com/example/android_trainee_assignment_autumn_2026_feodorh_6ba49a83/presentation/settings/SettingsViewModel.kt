package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model.AppColorScheme
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model.ThemeMode
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.GetColorSchemeUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.GetThemeModeUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.GigaChatGetBalanceUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.ResetSettingsUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.SetColorSchemeUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.SetThemeModeUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.settings.models.SettingsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getThemeModeUseCase: GetThemeModeUseCase,
    private val getColorSchemeUseCase: GetColorSchemeUseCase,
    private val saveThemeModeUseCase: SetThemeModeUseCase,
    private val saveColorSchemeUseCase: SetColorSchemeUseCase,
    private val resetSettingsUseCase: ResetSettingsUseCase,
    private val getBalanceUseCase: GigaChatGetBalanceUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<SettingsUiState>(SettingsUiState())
    val settingsUiState: StateFlow<SettingsUiState> = _state.asStateFlow()

    // Загрузка настроек при старте
    init {
        viewModelScope.launch {
            getThemeModeUseCase().collect { mode ->
                _state.update { it.copy(themeMode = mode) }
            }
        }
        viewModelScope.launch {
            getColorSchemeUseCase().collect { scheme ->
                _state.update { it.copy(colorScheme = scheme) }
            }
        }
    }

    fun updateThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            saveThemeModeUseCase(mode)
        }
    }

    fun updateColorScheme(scheme: AppColorScheme) {
        viewModelScope.launch {
            saveColorSchemeUseCase(scheme)
        }
    }

    fun resetSettings() {
        viewModelScope.launch {
            resetSettingsUseCase()
            _state.update {
                it.copy(
                    themeMode = ThemeMode.SYSTEM,
                    colorScheme = AppColorScheme.Default,
                    balance = null,
                    isBalanceLoading = false,
                    error = null
                )
            }
        }
    }

    fun loadBalance() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isBalanceLoading = true,
                    error = null
                )
            }
            try {
                val result = getBalanceUseCase()
                if (result != null) {
                    _state.update { it.copy(balance = result) }
                } else {
                    _state.update { it.copy(error = "Ошибка получения баланса") }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = "Ошибка загрузки баланса") }
            } finally {
                _state.update { it.copy(isBalanceLoading = false) }
            }
        }
    }
}
