package com.hanhyo.data.di

import android.content.Context
import androidx.room.Room
import com.hanhyo.data.local.db.PushupDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): PushupDatabase = Room.databaseBuilder(
        context = context,
        klass = PushupDatabase::class.java,
        name = "pushup_database"
    ).build()

    @Provides
    fun providePushupDao(database: PushupDatabase) = database.pushupSessionDao()
}
