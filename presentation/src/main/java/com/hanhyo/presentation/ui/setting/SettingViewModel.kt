package com.hanhyo.presentation.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hanhyo.domain.usecase.ObservePreferenceUseCase
import com.hanhyo.domain.usecase.UpdatePreferenceUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import timber.log.Timber
import java.io.IOException

class SettingViewModel(
    private val updatePreferenceUseCase: UpdatePreferenceUseCase,
    private val observePreferenceUseCase: ObservePreferenceUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingUiState())
    val uiState: StateFlow<SettingUiState> = _uiState.asStateFlow()


    init {
        observePreference()
    }


    private fun observePreference() {
        viewModelScope.launch {
            observePreferenceUseCase()
                .retry(3) { e ->
                    Timber.tag("SettingViewModel-observePreference").e(e, "설정 관찰 재시도 중")
                    delay(1000)
                    true
                }
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false) }
                    Timber.tag("SettingViewModel-observePreference").e(e, "설정을 가져오는 중 오류 발생")
                }
                .collect { preference ->
                    _uiState.update {
                        it.copy(
                            vibrationEnabled = preference.vibrationEnabled,
                            soundEnabled = preference.soundEnabled,
                            isLoading = false
                        )
                    }
                }
        }
    }

    private fun updatePreference(
        settingName: String,
        update: suspend () -> Unit
    ) {
        viewModelScope.launch {
            try {
                update()
            } catch (e: IOException) {
                Timber.tag("SettingViewModel-$settingName").e(e, "설정을 업데이트 중 오류 발생")
            } catch (e: SerializationException) {
                Timber.tag("SettingViewModel-$settingName").e(e, "직렬화 오류 발생")
            }
        }
    }

    fun updateVibrationEnabled(enabled: Boolean) {
        updatePreference("updateVibrationEnabled") {
            updatePreferenceUseCase.updateVibration(enabled)
        }
    }

    fun updateSoundEnabled(enabled: Boolean) {
        updatePreference("updateSoundEnabled") {
            updatePreferenceUseCase.updateSound(enabled)
        }
    }
}
