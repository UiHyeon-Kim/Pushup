package com.hanhyo.domain.repository

import com.hanhyo.domain.model.UserPreference
import kotlinx.coroutines.flow.Flow

interface PreferenceRepository {
    suspend fun updatePreference(preference: UserPreference)
    fun observePreference(): Flow<UserPreference>
}
