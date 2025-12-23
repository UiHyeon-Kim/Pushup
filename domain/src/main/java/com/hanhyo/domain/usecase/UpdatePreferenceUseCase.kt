package com.hanhyo.domain.usecase

import com.hanhyo.domain.repository.PreferenceRepository

class UpdatePreferenceUseCase(
    private val preferenceRepository: PreferenceRepository
) {
    suspend fun updateVibration(enabled: Boolean) {
        preferenceRepository.updateVibration(enabled)
    }

    suspend fun updateSound(enabled: Boolean) {
        preferenceRepository.updateSound(enabled)
    }
}
