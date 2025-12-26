package com.hanhyo.data.sensor.datasource

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.hanhyo.data.R
import com.hanhyo.domain.model.PushupState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class ProximitySensorDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
) : SensorDataSource {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

    private val proximityThreshold = proximitySensor?.let {
        it.maximumRange * PROXIMITY_THRESHOLD
    } ?: (MAX_RANGE * PROXIMITY_THRESHOLD)


    override fun observePushupState(): Flow<PushupState> = callbackFlow {

        if (proximitySensor == null) {
            trySend(PushupState.Unknown)
            close(IllegalStateException(context.getString(R.string.error_proximity_sensor_not_found)))
            return@callbackFlow
        }

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    if (it.sensor.type == Sensor.TYPE_PROXIMITY) {
                        val distance = it.values[0] // 거리는 2진 값으로 반환 됨

                        val state = if (distance < proximityThreshold) PushupState.Near else PushupState.Far
                        trySend(state)
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }

        sensorManager.registerListener(
            listener,
            proximitySensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )

        // Flow가 취소되면 센서 리스너 해제
        awaitClose {
            sensorManager.unregisterListener(listener)
        }
    }.distinctUntilChanged() // 동일한 상태는 필터링

    override fun isSensorAvailable(): Boolean = proximitySensor != null


    companion object {
        const val MAX_RANGE = 5f                // 기종에 따라 근접 센서 인식 범위는 5cm +- 3cm 정도
        const val PROXIMITY_THRESHOLD = 0.8f    // 80%
    }
}
