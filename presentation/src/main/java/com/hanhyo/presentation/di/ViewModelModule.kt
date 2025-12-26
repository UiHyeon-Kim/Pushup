package com.hanhyo.presentation.di


import com.hanhyo.presentation.ui.pushup.PushupViewModel
import com.hanhyo.presentation.ui.record.PushupRecordViewModel
import com.hanhyo.presentation.ui.setting.SettingViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        PushupViewModel(
            application = androidApplication(),
            observePushupStateUseCase = get(),
            startSessionUseCase = get(),
            updateSessionUseCase = get(),
            completeSessionUseCase = get(),
            checkSensorAvailableUseCase = get(),
            observePreferenceUseCase = get(),
        )
    }

    viewModel {
        PushupRecordViewModel(
            repository = get(),
        )
    }

    viewModel {
        SettingViewModel(
            updatePreferenceUseCase = get(),
            observePreferenceUseCase = get(),
        )
    }
}
