package com.hanhyo.presentation

import android.app.Application
import android.content.pm.ApplicationInfo
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class PushupApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val isDebuggable = (0 != (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE))
        if (isDebuggable) Timber.plant(Timber.DebugTree())
    }
}
