package com.hanhyo.presentation.ui.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.hanhyo.presentation.designsystem.theme.PushupTheme
import com.hanhyo.presentation.ui.setting.components.SettingSection
import com.hanhyo.presentation.ui.setting.components.SwitchSettingItem

@Composable
fun SettingScreen(
    viewModel: SettingViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    SettingContent(
        uiState = uiState,
        onVibrationChange = viewModel::updateVibrationEnabled,
        onSoundChange = viewModel::updateSoundEnabled,
    )

}

@Composable
fun SettingContent(
    uiState: SettingUiState,
    onVibrationChange: (Boolean) -> Unit,
    onSoundChange: (Boolean) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // 피드백 섹션
        SettingSection(title = "피드백") {
            SwitchSettingItem(
                title = "진동",
                subtitle = "푸시업 카운트 시 진동 피드백",
                checked = uiState.vibrationEnabled,
                onCheckedChange = onVibrationChange
            )

            HorizontalDivider()

            SwitchSettingItem(
                title = "사운드",
                subtitle = "푸시업 카운트 시 사운드 피드백",
                checked = uiState.soundEnabled,
                onCheckedChange = onSoundChange
            )
        }
    }
}

@Preview
@Composable
private fun SettingContentPreview() {
    PushupTheme {
        SettingContent(
            uiState = SettingUiState(),
            onVibrationChange = {},
            onSoundChange = {},
        )
    }
}
