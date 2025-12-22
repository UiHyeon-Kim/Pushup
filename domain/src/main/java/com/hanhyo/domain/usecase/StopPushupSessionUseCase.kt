package com.hanhyo.domain.usecase

import com.hanhyo.domain.repository.PushupSensorRepository

class StopPushupSessionUseCase(
    private val repository: PushupSensorRepository
) {
    suspend operator fun invoke() {
        repository.stopSensorMonitoring()
    }
}
