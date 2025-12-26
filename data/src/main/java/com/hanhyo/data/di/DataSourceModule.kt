package com.hanhyo.data.di

import com.hanhyo.data.local.datasource.PushupLocalDataSource
import com.hanhyo.data.sensor.datasource.ProximitySensorDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataSourceModule = module {

    single { ProximitySensorDataSource(androidContext()) }

    single { PushupLocalDataSource(get()) }
}
