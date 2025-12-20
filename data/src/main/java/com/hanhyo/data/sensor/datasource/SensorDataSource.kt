package com.hanhyo.data.sensor.datasource

import com.hanhyo.domain.model.PushupState
import kotlinx.coroutines.flow.Flow

/**
 * 센서 데이터 소스 인터페이스
 */
interface SensorDataSource {
    suspend fun startSensorMonitoring()
    suspend fun stopSensorMonitoring()
    fun observePushupState(): Flow<PushupState>
    fun isSensorAvailable(): Boolean
}
