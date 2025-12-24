package com.hanhyo.presentation.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hanhyo.domain.usecase.ObservePreferenceUseCase
import com.hanhyo.domain.usecase.UpdatePreferenceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
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

    fun updateVibrationEnabled(enabled: Boolean) {
        viewModelScope.launch {
            try {
                updatePreferenceUseCase.updateVibration(enabled)
            } catch (e: IOException) {
                Timber.tag("SettingViewModel-updateVibrationEnabled").e(e, "설정을 업데이트 중 오류 발생")
            }
        }
    }

    fun updateSoundEnabled(enabled: Boolean) {
        viewModelScope.launch {
            try {
                updatePreferenceUseCase.updateSound(enabled)
            } catch (e: IOException) {
                Timber.tag("SettingViewModel-updateSoundEnabled").e(e, "설정을 업데이트 중 오류 발생")
            }
        }
    }
}
