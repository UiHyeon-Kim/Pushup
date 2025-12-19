package com.hanhyo.data.repositoryimpl

import com.hanhyo.data.sensor.datasource.PushupSensorDataSource
import com.hanhyo.domain.repository.PushupSensorRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PushupSensorRepositoryImpl @Inject constructor(
    private val pushupSensorDataSource: PushupSensorDataSource
) : PushupSensorRepository {

    private var count = 0
    private var job: Job? = null

    override fun startMeasure(): Flow<Int> = channelFlow {
        count = 0

        job = launch {
            pushupSensorDataSource.observePushup().collect {
                count++
                send(count)
            }
        }

        awaitClose {
            job?.cancel()
        }
    }

    override fun stopMeasure() {
        job?.cancel()
        job = null
    }
}
