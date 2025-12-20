package com.hanhyo.presentation.ui.pushup

data class PushupUiState(
    val currentCount: Int = 0,
    val totalPushup: Int = 0,
    val isMeasuring: Boolean = false,
)
