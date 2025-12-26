package com.hanhyo.presentation.ui.record

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hanhyo.presentation.ui.record.components.SessionCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun PushupRecordScreen(
    viewModel: PushupRecordViewModel = koinViewModel()
) {
    val sessions by viewModel.sessions.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text(
                "저장된 세션 목록",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        items(sessions, key = { it.id ?: it.startTime }) { session ->
            SessionCard(session = session)
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (sessions.isEmpty()) {
            item {
                Text(
                    "저장된 세션이 없습니다.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
