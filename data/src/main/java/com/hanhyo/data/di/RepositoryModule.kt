package com.hanhyo.data.di

import android.content.Context
import com.hanhyo.data.local.datasource.PushupLocalDataSource
import com.hanhyo.data.local.datastore.PreferenceDataStore
import com.hanhyo.data.repositoryimpl.PreferenceRepositoryImpl
import com.hanhyo.data.repositoryimpl.PushupRecordRepositoryImpl
import com.hanhyo.data.repositoryimpl.PushupSensorRepositoryImpl
import com.hanhyo.data.sensor.datasource.ProximitySensorDataSource
import com.hanhyo.domain.repository.PreferenceRepository
import com.hanhyo.domain.repository.PushupRecordRepository
import com.hanhyo.domain.repository.PushupSensorRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun providePushupSensorRepository(proximitySensorDataSource: ProximitySensorDataSource): PushupSensorRepository =
        PushupSensorRepositoryImpl(proximitySensorDataSource)

    @Provides
    fun providePushupRecordRepository(pushupLocalDataSource: PushupLocalDataSource): PushupRecordRepository =
        PushupRecordRepositoryImpl(pushupLocalDataSource)

    @Provides
    fun providePreferenceRepository(preferenceDataStore: PreferenceDataStore): PreferenceRepository =
        PreferenceRepositoryImpl(preferenceDataStore)
}
