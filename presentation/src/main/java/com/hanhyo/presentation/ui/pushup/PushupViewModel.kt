package com.hanhyo.presentation.ui.pushup

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hanhyo.domain.model.PushupState
import com.hanhyo.domain.usecase.ObservePushupStateUseCase
import com.hanhyo.presentation.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

@HiltViewModel
class PushupViewModel @Inject constructor(
    private val application: Application,
    private val observePushupStateUseCase: ObservePushupStateUseCase,
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
            val isAvailable = try {
                withTimeout(SENSOR_TIMEOUT) {
                    observePushupStateUseCase().first()
                }
                true
            } catch (_: Exception) {
                false
            }
            _uiState.update {
                it.copy(
                    isSensorAvailable = isAvailable,
                    errorMessage = if (!isAvailable) application.getString(R.string.error_sensor_unavailable) else null
                )
            }
        }
    }

    fun startSession() {
        if (_uiState.value.isSessionActive) return
        if (!_uiState.value.isSensorAvailable) {
            _uiState.update { it.copy(errorMessage = application.getString(R.string.error_sensor_unavailable)) }
            return
        }

        viewModelScope.launch {
            try {
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

            } catch (e: IllegalStateException) {
                _uiState.update {
                    it.copy(
                        isSessionActive = false,
                        errorMessage = application.getString(R.string.error_session_start_failed, e.message)
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
                    it.copy(errorMessage = application.getString(R.string.error_sensor_monitoring, e.message))
                }
            } catch (e: CancellationException) {
                throw e
            }
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_uiState.value.isSessionActive) {
                delay(TIMER_INTERVAL)
                if (!_uiState.value.isSessionActive) break
                _uiState.update { currentState ->
                    currentState.sessionStartTime?.let { startTime ->
                        val duration = ((System.currentTimeMillis() - startTime) / MILLIS_PER_SECOND).toInt()
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
                pushupStateMonitoringJob?.cancel()
                timerJob?.cancel()

                _uiState.update {
                    it.copy(
                        isSessionActive = false,
                        pushupState = PushupState.Unknown,
                    )
                }
            } catch (e: IllegalStateException) {
                _uiState.update {
                    it.copy(
                        isSessionActive = false,
                        errorMessage = application.getString(R.string.error_session_stop_failed, e.message)
                    )
                }
            } catch (e: CancellationException) {
                throw e
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


    companion object {
        const val DEBOUNCE = 1000L
        const val SENSOR_TIMEOUT = 1000L
        const val TIMER_INTERVAL = 1000L
        const val MILLIS_PER_SECOND = 1000
    }
}
