package com.hanhyo.domain.repository

import com.hanhyo.domain.model.UserPreference
import kotlinx.coroutines.flow.Flow

interface PreferenceRepository {
    suspend fun updateVibration(enabled: Boolean)
    suspend fun updateSound(enabled: Boolean)
    fun observePreference(): Flow<UserPreference>
}
