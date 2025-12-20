package com.hanhyo.domain.usecase

import com.hanhyo.domain.repository.PushupSensorRepository
import kotlinx.coroutines.flow.Flow

class MeasurePushupUseCase(
    private val pushupSensorRepository: PushupSensorRepository
) {
    fun start(): Flow<Int> = pushupSensorRepository.startMeasure()
    fun stop(): Unit = pushupSensorRepository.stopMeasure()
}
