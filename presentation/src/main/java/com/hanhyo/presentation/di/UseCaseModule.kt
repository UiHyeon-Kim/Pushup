package com.hanhyo.presentation.di

import com.hanhyo.domain.repository.PushupSensorRepository
import com.hanhyo.domain.usecase.MeasurePushupUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideMeasurePushupUseCase(repository: PushupSensorRepository): MeasurePushupUseCase =
        MeasurePushupUseCase(repository)
}
