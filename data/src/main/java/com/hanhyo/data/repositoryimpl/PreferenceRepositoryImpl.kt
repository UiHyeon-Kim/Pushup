package com.hanhyo.data.repositoryimpl

import com.hanhyo.data.local.datastore.PreferenceDataStore
import com.hanhyo.domain.model.UserPreference
import com.hanhyo.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PreferenceRepositoryImpl @Inject constructor(
    private val preferenceDataStore: PreferenceDataStore
) : PreferenceRepository {

    override suspend fun updatePreference(preference: UserPreference) {
        preferenceDataStore.update(preference)
    }

    override fun observePreference(): Flow<UserPreference> = preferenceDataStore.preference
}
