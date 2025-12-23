package com.hanhyo.presentation.ui.pushup

import com.hanhyo.domain.model.PushupState

data class PushupUiState(
    val sessionId: Long? = null,
    val currentCount: Int = 0,
    val sessionStartTime: Long? = null,
    val sessionDuration: Int = 0,
    val isSessionActive: Boolean = false,
    val pushupState: PushupState = PushupState.Unknown,
    val isSensorAvailable: Boolean = true,
    val errorMessage: String? = null,
)
