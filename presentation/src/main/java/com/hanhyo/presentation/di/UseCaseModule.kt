package com.hanhyo.presentation.di

import com.hanhyo.domain.repository.PushupRecordRepository
import com.hanhyo.domain.repository.PushupSensorRepository
import com.hanhyo.domain.usecase.CheckSensorAvailableUseCase
import com.hanhyo.domain.usecase.CompleteSessionUseCase
import com.hanhyo.domain.usecase.ObservePushupStateUseCase
import com.hanhyo.domain.usecase.StartSessionUseCase
import com.hanhyo.domain.usecase.UpdateSessionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideObservePushupStateUseCase(repository: PushupSensorRepository): ObservePushupStateUseCase =
        ObservePushupStateUseCase(repository)

    @Provides
    fun provideStartSessionUseCase(repository: PushupRecordRepository): StartSessionUseCase =
        StartSessionUseCase(repository)

    @Provides
    fun provideUpdateSessionUseCase(repository: PushupRecordRepository): UpdateSessionUseCase =
        UpdateSessionUseCase(repository)

    @Provides
    fun provideCompleteSessionUseCase(repository: PushupRecordRepository): CompleteSessionUseCase =
        CompleteSessionUseCase(repository)

    @Provides
    fun provideCheckSensorAvailableUseCase(
        pushupSensorRepository: PushupSensorRepository
    ): CheckSensorAvailableUseCase {
        return CheckSensorAvailableUseCase(pushupSensorRepository)
    }
}
