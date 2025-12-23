package com.hanhyo.presentation.ui.record.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hanhyo.domain.model.PushupSession

@Composable
fun SessionCard(
    modifier: Modifier = Modifier,
    session: PushupSession
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "세션 ID: ${session.id}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text("타입: ${session.type}")
            Text("총 개수: ${session.totalCount}")
            Text("칼로리: ${session.calories}")
            Text("완료: ${if (session.isCompleted) "✓" else "✗"}")

            val startDateTime = java.text.SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                java.util.Locale.getDefault()
            ).format(java.util.Date(session.startTime))

            Text("시작: $startDateTime")

            session.endTime?.let { endTime ->
                val endDateTime = java.text.SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss",
                    java.util.Locale.getDefault()
                ).format(java.util.Date(endTime))
                Text("종료: $endDateTime")
            }
        }
    }
}
