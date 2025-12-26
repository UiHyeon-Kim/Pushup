package com.hanhyo.data.local.datastore

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.hanhyo.domain.model.UserPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException

private val Context.dataStore by preferencesDataStore(name = "preferences")

class PreferenceDataStore(
    private val context: Context,
) {

    val preference: Flow<UserPreference> = context.dataStore.data
        .catch { e ->
            Timber.tag("PreferenceDataStore").e(e, "설정을 가져오는 중 오류 발생")
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
            Timber.tag("PreferenceDataStore-updateVibration").e(e, "진동 설정을 업데이트하지 못했습니다")
            throw e
        } catch (e: CorruptionException) {
            Timber.tag("PreferenceDataStore-updateVibration").e(e, "진동을 업데이트하는 동안 데이터 저장소가 손상되었습니다")
            throw e
        }
    }

    suspend fun updateSound(enabled: Boolean) {
        try {
            context.dataStore.edit { prefer ->
                prefer[SOUND] = enabled
            }
        } catch (e: IOException) {
            Timber.tag("PreferenceDataStore-updateSound").e(e, "설정 업데이트 중 오류 발생")
            throw e
        } catch (e: CorruptionException) {
            Timber.tag("PreferenceDataStore-updateSound").e(e, "설정 업데이트 중 오류 발생")
            throw e
        }
    }

    companion object {
        val VIBRATION = booleanPreferencesKey("vibration")
        val SOUND = booleanPreferencesKey("sound")
    }
}
