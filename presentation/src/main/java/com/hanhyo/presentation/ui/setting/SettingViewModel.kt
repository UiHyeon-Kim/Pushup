package com.hanhyo.presentation.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hanhyo.domain.usecase.ObservePreferenceUseCase
import com.hanhyo.domain.usecase.UpdatePreferenceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
                    _uiState.update {
                        it.copy(
                            vibrationEnabled = it.vibrationEnabled,
                            soundEnabled = it.soundEnabled,
                            isLoading = false
                        )
                    }
                    e.printStackTrace()
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

    fun updateVibrationEnabled(vibrationEnabled: Boolean) {
        viewModelScope.launch {
            val preference = uiState.value.toUserPreference().copy(vibrationEnabled = vibrationEnabled)
            updatePreferenceUseCase(preference)
        }
    }

    fun updateSoundEnabled(soundEnabled: Boolean) {
        viewModelScope.launch {
            val preference = uiState.value.toUserPreference().copy(soundEnabled = soundEnabled)
            updatePreferenceUseCase(preference)
        }
    }
}
