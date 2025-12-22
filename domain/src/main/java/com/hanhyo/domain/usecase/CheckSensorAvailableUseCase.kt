package com.hanhyo.domain.usecase

import com.hanhyo.domain.repository.PushupSensorRepository

class CheckSensorAvailableUseCase(
    private val pushupSensorRepository: PushupSensorRepository
) {
    operator fun invoke(): Boolean {
        return pushupSensorRepository.isSensorAvailable()
    }
}
