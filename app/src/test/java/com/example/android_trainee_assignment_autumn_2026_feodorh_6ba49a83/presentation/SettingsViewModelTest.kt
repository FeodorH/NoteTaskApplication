package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation

import androidx.compose.material3.ColorScheme
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model.AppColorScheme
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model.ThemeMode
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.GetColorSchemeUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.GetThemeModeUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.GigaChatGetBalanceUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.ResetSettingsUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.SetColorSchemeUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.SetThemeModeUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.settings.SettingsViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SettingsViewModelTest {
    private lateinit var viewModel: SettingsViewModel
    private val getThemeModeUseCase: GetThemeModeUseCase = mockk()
    private val getColorSchemeUseCase: GetColorSchemeUseCase = mockk()
    private val saveThemeModeUseCase: SetThemeModeUseCase = mockk()
    private val saveColorSchemeUseCase: SetColorSchemeUseCase = mockk()
    private val resetSettingsUseCase: ResetSettingsUseCase = mockk()
    private val getBalanceUseCase: GigaChatGetBalanceUseCase = mockk()

    private val themeModeFlow = MutableStateFlow(ThemeMode.SYSTEM)
    private val colorSchemeFlow = MutableStateFlow(AppColorScheme.Default)
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach// TODO
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { getThemeModeUseCase() } returns themeModeFlow
        coEvery { getColorSchemeUseCase() } returns colorSchemeFlow
        coEvery { saveThemeModeUseCase(any()) } answers {
            val arg = firstArg<ThemeMode>()
            themeModeFlow.value = arg
        }
        coEvery { saveColorSchemeUseCase(any()) } answers {
            val arg = firstArg<AppColorScheme>()
            colorSchemeFlow.value = arg
        }
        coEvery { resetSettingsUseCase() } answers {
            themeModeFlow.value = ThemeMode.SYSTEM
            colorSchemeFlow.value = AppColorScheme.Default
        }
        coEvery { getBalanceUseCase() } returns 100.0

        viewModel = SettingsViewModel(
            getThemeModeUseCase = getThemeModeUseCase,
            getColorSchemeUseCase = getColorSchemeUseCase,
            saveThemeModeUseCase = saveThemeModeUseCase,
            saveColorSchemeUseCase = saveColorSchemeUseCase,
            resetSettingsUseCase = resetSettingsUseCase,
            getBalanceUseCase = getBalanceUseCase
        )
    }

    @Test
    fun `updateThemeMode should save new theme and update state`() = runTest(testDispatcher) {
        advanceUntilIdle()

        // when
        viewModel.updateThemeMode(ThemeMode.DARK)
        advanceUntilIdle()

        // then
        val state = viewModel.settingsUiState.value
        assertEquals(ThemeMode.DARK, state.themeMode)
        coVerify { saveThemeModeUseCase(ThemeMode.DARK) }
    }

    @Test
    fun `resetSettings should clear preferences and reset state to defaults`() = runTest(testDispatcher) {
        // сначала меняем тему
        viewModel.updateThemeMode(ThemeMode.DARK)
        advanceUntilIdle()

        // when
        viewModel.resetSettings()
        advanceUntilIdle()

        // then
        val state = viewModel.settingsUiState.value
        assertEquals(ThemeMode.SYSTEM, state.themeMode)
        assertEquals(AppColorScheme.Default, state.colorScheme)
        coVerify { resetSettingsUseCase() }
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }
}