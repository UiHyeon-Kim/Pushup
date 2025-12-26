package com.hanhyo.app

import android.app.Application
import com.hanhyo.data.di.dataSourceModule
import com.hanhyo.data.di.databaseModule
import com.hanhyo.data.di.repositoryModule
import com.hanhyo.domain.di.useCaseModule
import com.hanhyo.presentation.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class PushupApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Timber 초기화
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        // Koin 초기화
        startKoin {
            val loggerLevel = if (BuildConfig.DEBUG) Level.DEBUG else Level.NONE
            androidLogger(loggerLevel)
            androidContext(this@PushupApplication)
            modules(
                databaseModule,
                dataSourceModule,
                repositoryModule,
                useCaseModule,
                viewModelModule
            )
        }
    }
}
