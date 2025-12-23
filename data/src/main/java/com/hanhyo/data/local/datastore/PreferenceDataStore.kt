package com.hanhyo.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.hanhyo.domain.model.UserPreference
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "preferences")

class PreferenceDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    val preference: Flow<UserPreference> = context.dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { preferences ->
            UserPreference(
                vibrationEnabled = preferences[VIBRATION] ?: true,
                soundEnabled = preferences[SOUND] ?: true,
            )
        }

    suspend fun updateVibration(enabled: Boolean) {
        context.dataStore.edit { prefer ->
            prefer[VIBRATION] = enabled
        }
    }

    suspend fun updateSound(enabled: Boolean) {
        context.dataStore.edit { prefer ->
            prefer[SOUND] = enabled
        }
    }

    companion object Keys {
        val VIBRATION = booleanPreferencesKey("vibration")
        val SOUND = booleanPreferencesKey("sound")
    }
}
