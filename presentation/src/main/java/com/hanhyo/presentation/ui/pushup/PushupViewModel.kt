package com.hanhyo.presentation.ui.pushup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hanhyo.domain.usecase.MeasurePushupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PushupViewModel @Inject constructor(
    private val measurePushupUseCase: MeasurePushupUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PushupUiState())
    val uiState: StateFlow<PushupUiState> = _uiState.asStateFlow()

    fun startMeasure() {
        viewModelScope.launch {
            measurePushupUseCase.start().collect { count ->
                _uiState.update { it.copy(currentCount = count, isMeasuring = true) }
            }
        }
    }

    fun stopMeasure() {
        measurePushupUseCase.stop()
        _uiState.update { it.copy(isMeasuring = false) }
    }
}
