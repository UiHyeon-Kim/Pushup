package com.hanhyo.data.repositoryimpl

import com.hanhyo.data.sensor.datasource.ProximitySensorDataSource
import com.hanhyo.domain.model.PushupState
import com.hanhyo.domain.repository.PushupSensorRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PushupSensorRepositoryImpl @Inject constructor(
    private val proximitySensorDataSource: ProximitySensorDataSource
) : PushupSensorRepository {

    override fun observePushupState(): Flow<PushupState> = proximitySensorDataSource.observePushupState()

    override fun isSensorAvailable(): Boolean = proximitySensorDataSource.isSensorAvailable()
}
