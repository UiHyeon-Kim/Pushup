package com.hanhyo.data.di

import androidx.room.Room
import com.hanhyo.data.local.datastore.PreferenceDataStore
import com.hanhyo.data.local.db.PushupDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {

    single {
        Room.databaseBuilder(
            context = androidContext(),
            klass = PushupDatabase::class.java,
            name = "pushup_database"
        ).build()
    }

    single { get<PushupDatabase>().pushupSessionDao() }

    single { PreferenceDataStore(androidContext()) }
}
