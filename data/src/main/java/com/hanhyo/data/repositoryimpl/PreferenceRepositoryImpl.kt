package com.hanhyo.data.repositoryimpl

import com.hanhyo.data.local.datastore.PreferenceDataStore
import com.hanhyo.domain.model.UserPreference
import com.hanhyo.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.Flow

class PreferenceRepositoryImpl(
    private val preferenceDataStore: PreferenceDataStore
) : PreferenceRepository {

    override suspend fun updateVibration(enabled: Boolean) {
        preferenceDataStore.updateVibration(enabled)
    }

    override suspend fun updateSound(enabled: Boolean) {
        preferenceDataStore.updateSound(enabled)
    }

    override fun observePreference(): Flow<UserPreference> = preferenceDataStore.preference
}
