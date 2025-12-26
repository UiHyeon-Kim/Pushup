package com.hanhyo.domain.di

import com.hanhyo.domain.usecase.CheckSensorAvailableUseCase
import com.hanhyo.domain.usecase.CompleteSessionUseCase
import com.hanhyo.domain.usecase.ObservePreferenceUseCase
import com.hanhyo.domain.usecase.ObservePushupStateUseCase
import com.hanhyo.domain.usecase.StartSessionUseCase
import com.hanhyo.domain.usecase.UpdatePreferenceUseCase
import com.hanhyo.domain.usecase.UpdateSessionUseCase
import org.koin.dsl.module

val useCaseModule = module {

    factory { ObservePushupStateUseCase(get()) }

    factory { StartSessionUseCase(get()) }
    
    factory { UpdateSessionUseCase(get()) }

    factory { CompleteSessionUseCase(get()) }

    factory { CheckSensorAvailableUseCase(get()) }

    factory { ObservePreferenceUseCase(get()) }

    factory { UpdatePreferenceUseCase(get()) }

}
