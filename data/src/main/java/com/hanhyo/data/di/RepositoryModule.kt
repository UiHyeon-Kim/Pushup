package com.hanhyo.data.di

import com.hanhyo.data.repositoryimpl.PushupSensorRepositoryImpl
import com.hanhyo.data.sensor.datasource.PushupSensorDataSource
import com.hanhyo.domain.repository.PushupSensorRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun providePushupSensorRepository(dataSource: PushupSensorDataSource): PushupSensorRepository =
        PushupSensorRepositoryImpl(dataSource)
}
