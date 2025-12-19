package com.hanhyo.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hanhyo.data.local.dao.PushupSessionDao
import com.hanhyo.data.local.entity.PushupSessionEntity

@Database(
    entities = [
        PushupSessionEntity::class,
    ],
    version = 1,
)
abstract class PushupDatabase : RoomDatabase() {
    abstract fun pushupSessionDao(): PushupSessionDao
}
