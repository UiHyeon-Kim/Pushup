package com.hanhyo.domain.usecase

import com.hanhyo.domain.repository.PushupRecordRepository

class UpdateSessionUseCase(
    private val pushupRecordRepository: PushupRecordRepository
) {
    suspend operator fun invoke(sessionId: Long, count: Int) {
        pushupRecordRepository.updateSession(sessionId, count)
    }
}
