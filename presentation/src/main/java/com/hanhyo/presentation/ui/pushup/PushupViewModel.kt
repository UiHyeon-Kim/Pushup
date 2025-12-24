package com.hanhyo.presentation.ui.pushup

import android.app.Application
import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hanhyo.domain.model.PushupState
import com.hanhyo.domain.model.PushupType
import com.hanhyo.domain.model.UserPreference
import com.hanhyo.domain.usecase.CheckSensorAvailableUseCase
import com.hanhyo.domain.usecase.CompleteSessionUseCase
import com.hanhyo.domain.usecase.ObservePreferenceUseCase
import com.hanhyo.domain.usecase.ObservePushupStateUseCase
import com.hanhyo.domain.usecase.StartSessionUseCase
import com.hanhyo.domain.usecase.UpdateSessionUseCase
import com.hanhyo.presentation.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PushupViewModel @Inject constructor(
    private val application: Application,
    private val observePushupStateUseCase: ObservePushupStateUseCase,
    private val startSessionUseCase: StartSessionUseCase,
    private val updateSessionUseCase: UpdateSessionUseCase,
    private val completeSessionUseCase: CompleteSessionUseCase,
    private val checkSensorAvailableUseCase: CheckSensorAvailableUseCase,
    private val observePreferenceUseCase: ObservePreferenceUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PushupUiState())
    val uiState: StateFlow<PushupUiState> = _uiState.asStateFlow()

    private var pushupStateMonitoringJob: Job? = null
    private var timerJob: Job? = null

    private val debounceTime = DEBOUNCE
    private var lastCountTime = 0L
    private var sessionStartTime = 0L

    private var currentPreference = UserPreference()


    init {
        checkSensorAvailability()
        observePreferences()
    }


    private fun observePreferences() {
        viewModelScope.launch {
            observePreferenceUseCase().collect { preference ->
                currentPreference = preference
            }
        }
    }

    private fun checkSensorAvailability() {
        val isAvailable = checkSensorAvailableUseCase()
        _uiState.update {
            it.copy(
                isSensorAvailable = isAvailable,
                errorMessage = if (!isAvailable) application.getString(R.string.error_sensor_unavailable) else null
            )
        }
    }

    fun startSession(type: PushupType = PushupType.BASIC) {
        if (_uiState.value.isSessionActive) return
        if (!_uiState.value.isSensorAvailable) {
            _uiState.update { it.copy(errorMessage = application.getString(R.string.error_sensor_unavailable)) }
            return
        }

        viewModelScope.launch {
            try {
                val sessionId = startSessionUseCase(type)
                sessionStartTime = System.currentTimeMillis()

                _uiState.update {
                    it.copy(
                        sessionId = sessionId,
                        isSessionActive = true,
                        currentCount = 0,
                        sessionStartTime = sessionStartTime,
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

                        provideFeedback()

                        _uiState.value.sessionId?.let { sessionId ->
                            try {
                                updateSessionUseCase(sessionId, 1)
                            } catch (e: IllegalStateException) {
                                Timber.tag("PushupViewModel-startPushupMonitoring").e("세션을 업데이트 중 오류 발생: ${e.message}")
                            }
                        }
                    }

                    lastState = currentState
                }
            } catch (e: IllegalStateException) {
                _uiState.update {
                    it.copy(errorMessage = application.getString(R.string.error_sensor_monitoring, e.message))
                }
            }
        }
    }

    private fun provideFeedback() {
        if (currentPreference.vibrationEnabled) {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = application.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                application.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }

            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        }

        if (currentPreference.soundEnabled) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val toneGenerator = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100)
                    toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP2, 100)
                    delay(100)
                    toneGenerator.release()
                } catch (e: Exception) {
                    Timber.tag("PushupViewModel-provideFeedback").e("톤 생성 실패: ${e.message}")
                }
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
                _uiState.value.sessionId?.let { completeSessionUseCase(it) }

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
            }
        }
    }

    fun resetCount() {
        viewModelScope.launch {
            try {
                _uiState.value.sessionId?.let { completeSessionUseCase(it) }

                val newId = startSessionUseCase(PushupType.BASIC)
                sessionStartTime = System.currentTimeMillis()

                _uiState.update {
                    it.copy(
                        sessionId = newId,
                        currentCount = 0,
                        sessionDuration = 0,
                        sessionStartTime = sessionStartTime,
                    )
                }

                lastCountTime = 0L
            } catch (e: IllegalStateException) {
                _uiState.update {
                    it.copy(errorMessage = e.message)
                }
            }
        }
    }


    companion object {
        const val DEBOUNCE = 1000L
        const val TIMER_INTERVAL = 1000L
        const val MILLIS_PER_SECOND = 1000
    }
}
