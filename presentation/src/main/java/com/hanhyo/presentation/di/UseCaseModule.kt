package com.hanhyo.presentation.di

import com.hanhyo.domain.repository.PushupSensorRepository
import com.hanhyo.domain.usecase.ObservePushupStateUseCase
import com.hanhyo.domain.usecase.StopPushUpSessionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Provides
    fun provideStopPushUpSessionUseCase(repository: PushupSensorRepository): StopPushUpSessionUseCase =
        StopPushUpSessionUseCase(repository)

    @Provides
    fun provideObservePushupStateUseCase(repository: PushupSensorRepository): ObservePushupStateUseCase =
        ObservePushupStateUseCase(repository)
}
