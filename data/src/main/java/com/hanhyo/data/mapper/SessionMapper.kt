package com.hanhyo.data.mapper

import com.hanhyo.data.local.entity.PushupSessionEntity
import com.hanhyo.domain.model.PushupSession
import com.hanhyo.domain.model.PushupType

fun PushupSessionEntity.toDomain(): PushupSession =
    PushupSession(
        id = id,
        type = PushupType.valueOf(type),
        startTime = startTime,
        endTime = endTime,
        totalCount = totalCount,
        isCompleted = isCompleted,
        maxConsecutive = maxConsecutive,
        calories = calories
    )

fun PushupSession.toEntity(): PushupSessionEntity =
    PushupSessionEntity(
        id = id,
        type = type.name,
        startTime = startTime,
        endTime = endTime ?: 0L,
        totalCount = totalCount,
        isCompleted = isCompleted,
        maxConsecutive = maxConsecutive,
        calories = calories
    )


