package com.hanhyo.presentation.ui.pushup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hanhyo.domain.model.PushupState
import com.hanhyo.domain.usecase.ObservePushupStateUseCase
import com.hanhyo.domain.usecase.StopPushupSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PushupViewModel @Inject constructor(
    private val observePushupStateUseCase: ObservePushupStateUseCase,
    private val stopPushUpSessionUseCase: StopPushupSessionUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PushupUiState())
    val uiState: StateFlow<PushupUiState> = _uiState.asStateFlow()

    private var pushupStateMonitoringJob: Job? = null
    private var timerJob: Job? = null

    private var lastCountTime = 0L
    private val debounceTime = DEBOUNCE


    init {
        checkSensorAvailability()
    }


    private fun checkSensorAvailability() {
        viewModelScope.launch {
            try {
                observePushupStateUseCase().first()

                _uiState.update { it.copy(isSensorAvailable = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isSensorAvailable = false, errorMessage = "센서를 사용할 수 없습니다") }
            }
        }
    }

    fun startSession() {
        if (_uiState.value.isSessionActive) return
        if (!_uiState.value.isSensorAvailable) {
            _uiState.update { it.copy(errorMessage = "센서를 사용할 수 없습니다") }
            return
        }

        viewModelScope.launch {
            try {
                startPushUpSessionUseCase()

                _uiState.update {
                    it.copy(
                        isSessionActive = true,
                        currentCount = 0,
                        sessionStartTime = System.currentTimeMillis(),
                        sessionDuration = 0,
                        errorMessage = null
                    )
                }

                startPushupMonitoring()
                startTimer()

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSessionActive = false,
                        errorMessage = "세션 시작 실패: ${e.message}"
                    )
                }
            }
        }
    }

    private fun startPushupMonitoring() {
        pushupStateMonitoringJob?.cancel()
        pushupStateMonitoringJob = viewModelScope.launch {
            try {
                var lastState: PushupState = PushupState.Unknown

                observePushupStateUseCase().collect { currentState ->
                    _uiState.update { it.copy(pushupState = currentState) }

                    val now = System.currentTimeMillis()

                    if (lastState is PushupState.Far &&
                        currentState is PushupState.Near &&
                        now - lastCountTime > debounceTime
                    ) {
                        _uiState.update { it.copy(currentCount = it.currentCount + 1) }
                        lastCountTime = now
                    }

                    lastState = currentState
                }
            } catch (e: IllegalStateException) {
                _uiState.update {
                    it.copy(errorMessage = "센서 모니터링 오류: ${e.message}")
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "카운트 모니터링 오류: ${e.message}")
                }
            }
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_uiState.value.isSessionActive) {
                delay(1000L)
                _uiState.update { currentState ->
                    currentState.sessionStartTime?.let { startTime ->
                        val duration = ((System.currentTimeMillis() - startTime) / 1000).toInt()
                        currentState.copy(sessionDuration = duration)
                    } ?: currentState
                }
            }
        }
    }

    fun stopSession() {
        if (!_uiState.value.isSessionActive) return

        viewModelScope.launch {
            try {
                stopPushUpSessionUseCase()

                pushupStateMonitoringJob?.cancel()
                timerJob?.cancel()

                _uiState.update {
                    it.copy(
                        isSessionActive = false,
                        pushupState = PushupState.Unknown,
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "세션 종료 실패: ${e.message}")
                }
            }
        }
    }

    fun resetCount() {
        _uiState.update {
            it.copy(
                currentCount = 0,
                sessionStartTime = if (it.isSessionActive) System.currentTimeMillis() else null,
                sessionDuration = 0,
            )
        }

        lastCountTime = 0L
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            try {
                stopPushUpSessionUseCase()
                pushupStateMonitoringJob?.cancel()
                timerJob?.cancel()
            } catch (_: Exception) { }
        }
    }

    companion object {
        const val DEBOUNCE = 1000L
    }
}
