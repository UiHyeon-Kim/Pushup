package com.hanhyo.domain.usecase

import com.hanhyo.domain.repository.PushupRecordRepository

class CompleteSessionUseCase(
    private val pushupRecordRepository: PushupRecordRepository
) {
    suspend operator fun invoke(sessionId: Long) {
        pushupRecordRepository.endSession(sessionId)
    }
}
