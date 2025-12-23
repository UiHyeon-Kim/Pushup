package com.hanhyo.domain.usecase

import com.hanhyo.domain.repository.PushupRecordRepository

class UpdateSessionUseCase(
    private val pushupRecordRepository: PushupRecordRepository
) {
    suspend operator fun invoke(sessionId: Long, count: Int) {
        require(sessionId > 0) { "세션 ID는 0보다 커야합니다" }
        require(count >= 0) { "count는 음수일 수 없습니다" }
        pushupRecordRepository.updateSession(sessionId, count)
    }
}
