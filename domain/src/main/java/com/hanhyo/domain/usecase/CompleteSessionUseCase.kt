package com.hanhyo.domain.usecase

import com.hanhyo.domain.repository.PushupRecordRepository

class CompleteSessionUseCase(
    private val pushupRecordRepository: PushupRecordRepository
) {
    suspend operator fun invoke(sessionId: Long) {
        require(sessionId > 0) { "세션 ID는 0보다 커야합니다" }
        pushupRecordRepository.endSession(sessionId)
    }
}
