package com.hanhyo.presentation.ui.pushup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.hanhyo.domain.model.PushupState
import com.hanhyo.presentation.designsystem.theme.PushupTheme
import com.hanhyo.presentation.ui.pushup.components.ControlButtons
import com.hanhyo.presentation.ui.pushup.components.PushupCountCard
import com.hanhyo.presentation.ui.pushup.components.SensorStateCard

@Composable
fun PushupScreen(
    viewModel: PushupViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    PushupContent(
        currentCount = uiState.currentCount,
        isSessionActive = uiState.isSessionActive,
        pushupState = uiState.pushupState,
        onStart = viewModel::startSession,
        onStop = viewModel::stopSession,
        onReset = viewModel::resetCount,
    )
}

@Composable
fun PushupContent(
    modifier: Modifier = Modifier,
    currentCount: Int,
    isSessionActive: Boolean,
    pushupState: PushupState,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onReset: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        SensorStateCard(pushupState = pushupState)

        PushupCountCard(currentCount)

        ControlButtons(
            isSessionActive = isSessionActive,
            onStart = onStart,
            onStop = onStop,
            onReset = onReset,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PushupScreenPreview() {
    PushupTheme {
        PushupContent(
            currentCount = 0,
            isSessionActive = false,
            pushupState = PushupState.Unknown,
            onStart = {},
            onStop = {},
            onReset = {},
        )
    }
}
