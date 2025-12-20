package com.hanhyo.data.mapper

import com.hanhyo.data.local.entity.PushupSessionEntity
import com.hanhyo.domain.model.PushupSession
import com.hanhyo.domain.model.PushupType

fun PushupSessionEntity.toDomain(): PushupSession =
    PushupSession(
        id = id,
        type = runCatching { PushupType.valueOf(type) }
            .getOrDefault(PushupType.UNKNOWN),
        startTime = startTime,
        endTime = endTime,
        totalCount = totalCount,
        isCompleted = isCompleted,
        maxConsecutive = maxConsecutive,
        calories = calories
    )

fun PushupSession.toEntity(): PushupSessionEntity =
    PushupSessionEntity(
        id = id ?: 0, // Room의 autoGenerate 가 0 을 생성되지 않은 ID로 판단해 새 ID 생성
        type = type.name,
        startTime = startTime,
        endTime = endTime,
        totalCount = totalCount,
        isCompleted = isCompleted,
        maxConsecutive = maxConsecutive,
        calories = calories
    )


