package com.hanhyo.domain.usecase

import com.hanhyo.domain.model.PushupType
import com.hanhyo.domain.repository.PushupRecordRepository

class StartSessionUseCase(
    private val pushupRecordRepository: PushupRecordRepository
) {
    suspend operator fun invoke(type: PushupType): Long =
        pushupRecordRepository.startSession(type)
}
