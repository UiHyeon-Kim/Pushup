package com.hanhyo.domain.usecase

import com.hanhyo.domain.model.UserPreference
import com.hanhyo.domain.repository.PreferenceRepository

class UpdatePreferenceUseCase(
    private val preferenceRepository: PreferenceRepository
) {
    suspend operator fun invoke(preference: UserPreference) {
        preferenceRepository.updatePreference(preference)
    }
}
