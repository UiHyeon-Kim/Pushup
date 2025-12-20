package com.hanhyo.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hanhyo.data.local.dao.PushupSessionDao
import com.hanhyo.data.local.entity.PushupSessionEntity

@Database(
    entities = [
        PushupSessionEntity::class,
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class PushupDatabase : RoomDatabase() {
    abstract fun pushupSessionDao(): PushupSessionDao
}
