package com.hanhyo.domain.model

/**
 * 푸쉬업 세션 도메인 모델
 *
 * @property id 세션 ID (저장 후 할당)
 * @property type 푸쉬업 타입
 * @property startTime 시작 시간
 * @property endTime 종료 시간
 * @property totalCount 총 횟수
 * @property isCompleted 운동 종료 여부
 * @property maxConsecutive 최대 연속 횟수
 * @property calories 소모 칼로리
 */
data class PushupSession(
    val id: Long? = null,
    val type: PushupType,
    val startTime: Long,
    val endTime: Long?,
    val totalCount: Int,
    val isCompleted: Boolean,
    val maxConsecutive: Int,
    val calories: Int,
)
