package com.hanhyo.data.sensor.datasource

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.hanhyo.domain.model.PushupState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class PushupSensorDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    fun observePushup(): Flow<Unit> = callbackFlow {

        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) ?: error("센서를 찾을 수 없습니다")

        var currentState = PushupState.UP

        val eventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                val distance = event?.values[0] ?: return
                val isNear = distance < sensor.maximumRange

                when {
                    currentState == PushupState.UP && isNear -> {
                        currentState = PushupState.DOWN
                    }

                    currentState == PushupState.DOWN && !isNear -> {
                        currentState = PushupState.UP
                        trySend(Unit)
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }

        sensorManager.registerListener(eventListener, sensor, SensorManager.SENSOR_DELAY_GAME)

        awaitClose {
            sensorManager.unregisterListener(eventListener)
        }
    }
}
