package com.hanhyo.domain.repository

import com.hanhyo.domain.model.PushupState
import kotlinx.coroutines.flow.Flow

interface PushupSensorRepository {
    suspend fun stopSensorMonitoring()
    fun observePushupState(): Flow<PushupState>
    fun isSensorAvailable(): Boolean
}
