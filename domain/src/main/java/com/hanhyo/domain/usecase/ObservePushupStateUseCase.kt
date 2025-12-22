package com.hanhyo.domain.usecase

import com.hanhyo.domain.model.PushupState
import com.hanhyo.domain.repository.PushupSensorRepository
import kotlinx.coroutines.flow.Flow

class ObservePushupStateUseCase(
    private val repository: PushupSensorRepository
) {
    operator fun invoke(): Flow<PushupState> = repository.observePushupState()
}
