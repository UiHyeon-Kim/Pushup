package com.hanhyo.presentation

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
@Suppress("EmptyClassBlock") // 내부 구현 사항 생기기 전까지 Detekt 무시
class PushupApplication : Application() 
