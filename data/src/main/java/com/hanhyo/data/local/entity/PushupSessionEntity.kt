package com.hanhyo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pushup_session")
data class PushupSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,           // 세션 ID
    val type: String,           // 푸쉬업 타입
    val startTime: Long,        // 시작 시간
    val endTime: Long,          // 종료 시간
    val totalCount: Int,        // 총 횟수
    val isCompleted: Boolean,   // 운동 종료 여부
    val maxConsecutive: Int,    // 최대 연속 횟수
    val calories: Int,          // 소모 칼로리
)
