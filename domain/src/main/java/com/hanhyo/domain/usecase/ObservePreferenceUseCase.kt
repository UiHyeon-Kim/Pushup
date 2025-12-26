package com.hanhyo.domain.usecase

import com.hanhyo.domain.model.UserPreference
import com.hanhyo.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.Flow

class ObservePreferenceUseCase(
    private val preferenceRepository: PreferenceRepository
) {
    operator fun invoke(): Flow<UserPreference> = preferenceRepository.observePreference()
}
