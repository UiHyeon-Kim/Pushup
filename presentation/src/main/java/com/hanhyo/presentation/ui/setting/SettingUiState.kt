package com.hanhyo.presentation.ui.setting

import com.hanhyo.domain.model.UserPreference

data class SettingUiState(
    val vibrationEnabled: Boolean = true,
    val soundEnabled: Boolean = true,
    val isLoading: Boolean = true,
) {
    fun toUserPreference() = UserPreference(
        vibrationEnabled = vibrationEnabled,
        soundEnabled = soundEnabled,
    )
}
