package com.hanhyo.domain.repository

import kotlinx.coroutines.flow.Flow

interface PushupSensorRepository {
    fun startMeasure(): Flow<Int>
    fun stopMeasure()
}
