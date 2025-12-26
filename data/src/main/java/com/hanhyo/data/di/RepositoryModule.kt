package com.hanhyo.data.di

import com.hanhyo.data.repositoryimpl.PreferenceRepositoryImpl
import com.hanhyo.data.repositoryimpl.PushupRecordRepositoryImpl
import com.hanhyo.data.repositoryimpl.PushupSensorRepositoryImpl
import com.hanhyo.domain.repository.PreferenceRepository
import com.hanhyo.domain.repository.PushupRecordRepository
import com.hanhyo.domain.repository.PushupSensorRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<PreferenceRepository> { PreferenceRepositoryImpl(get()) }

    single<PushupRecordRepository> { PushupRecordRepositoryImpl(get()) }

    single<PushupSensorRepository> { PushupSensorRepositoryImpl(get()) }
}
