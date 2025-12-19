package com.hanhyo.presentation.ui.pushup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun PushupScreen(
    viewModel: PushupViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 현재 푸쉬업 개수
        Text(
            text = "푸쉬업 횟수: ${uiState.currentCount}",
            fontSize = 32.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 측정 시작 버튼
        Button(
            onClick = { viewModel.startMeasure() },
            enabled = !uiState.isMeasuring
        ) {
            Text("측정 시작")
        }

        // 측정 종료 버튼
        Button(
            onClick = { viewModel.stopMeasure() },
            enabled = uiState.isMeasuring
        ) {
            Text("측정 종료")
        }
    }
}
