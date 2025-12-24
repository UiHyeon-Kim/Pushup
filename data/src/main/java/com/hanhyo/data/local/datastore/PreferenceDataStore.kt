package com.hanhyo.data.local.datastore

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.hanhyo.domain.model.UserPreference
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "preferences")

class PreferenceDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    val preference: Flow<UserPreference> = context.dataStore.data
        .catch {
            Timber.tag("PreferenceDataStore").e(it.message)
            emit(emptyPreferences())
        }
        .map { preferences ->
            UserPreference(
                vibrationEnabled = preferences[VIBRATION] ?: true,
                soundEnabled = preferences[SOUND] ?: true,
            )
        }

    suspend fun updateVibration(enabled: Boolean) {
        try {
            context.dataStore.edit { prefer ->
                prefer[VIBRATION] = enabled
            }
        } catch (e: IOException) {
            Timber.tag("PreferenceDataStore-updateVibration").e(e.message)
            throw IllegalStateException()
        }
    }

    suspend fun updateSound(enabled: Boolean) {
        try {
            context.dataStore.edit { prefer ->
                prefer[SOUND] = enabled
            }
        } catch (e: IOException) {
            Timber.tag("PreferenceDataStore-updateSound").e(e, e.message)
            throw IllegalStateException()
        } catch (e: CorruptionException) {
            Timber.tag("PreferenceDataStore-updateSound").e(e, e.message)
            throw IllegalStateException()
        }
    }

    companion object {
        val VIBRATION = booleanPreferencesKey("vibration")
        val SOUND = booleanPreferencesKey("sound")
    }
}
