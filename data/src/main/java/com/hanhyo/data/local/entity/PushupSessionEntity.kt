package com.hanhyo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 푸쉬업 세션 엔티티
 * @param id 세션 ID
 * @param type 푸쉬업 타입
 * @param startTime 시작 시간
 * @param endTime 종료 시간
 * @param totalCount 총 횟수
 * @param isCompleted 운동 종료 여부
 * @param maxConsecutive 최대 연속 횟수
 * @param calories 소모 칼로리
 */
@Entity(tableName = "pushup_session")
data class PushupSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String,
    val startTime: Long,
    val endTime: Long?,
    val totalCount: Int,
    val isCompleted: Boolean,
    val maxConsecutive: Int,
    val calories: Int,
)
