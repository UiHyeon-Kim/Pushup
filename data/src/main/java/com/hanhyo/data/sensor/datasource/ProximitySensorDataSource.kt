package com.hanhyo.data.sensor.datasource

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.hanhyo.data.R
import com.hanhyo.domain.model.PushupState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

class ProximitySensorDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
) : SensorDataSource {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    val proximityThreshold = proximitySensor?.let {
        it.maximumRange * PROXIMITY_THRESHOLD
    } ?: (MAX_RANGE * PROXIMITY_THRESHOLD)

    private var sensorEventListener: SensorEventListener? = null


    fun stopSensorMonitoring() {
        sensorEventListener?.let {
            sensorManager.unregisterListener(it)
            sensorEventListener = null
        }
    }

    override fun observePushupState(): Flow<PushupState> = callbackFlow {

        if (proximitySensor == null) {
            trySend(PushupState.Unknown)
            close(IllegalStateException(context.getString(R.string.error_proximity_sensor_not_found)))
            return@callbackFlow
        }

        sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    if (it.sensor.type == Sensor.TYPE_PROXIMITY) {
                        val distance = it.values[0]

                        val state = if (distance < proximityThreshold) PushupState.Near else PushupState.Far
                        trySend(state)
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }

        sensorManager.registerListener(
            sensorEventListener,
            proximitySensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )

        // Flow가 취소되면 센서 리스너 해제
        awaitClose {
            sensorManager.unregisterListener(sensorEventListener)
            sensorEventListener = null
        }
    }.distinctUntilChanged() // 동일한 상태는 필터링
        // Hot Flow로 변경
        .shareIn(
            scope = serviceScope, // 이 Flow를 유지하고 있을 범위
            started = SharingStarted.WhileSubscribed(5000), // 구독자가 사라지고 5초 후 센서 종료
            replay = 1, // 새 구독자에게 가장 최근 센서 상태 전달
        )

    override fun isSensorAvailable(): Boolean = proximitySensor != null


    companion object {
        const val MAX_RANGE = 5f                // 기종에 따라 근접 센서 인식 범위는 5cm +- 3cm 정도
        const val PROXIMITY_THRESHOLD = 0.8f    // 80%
    }
}
