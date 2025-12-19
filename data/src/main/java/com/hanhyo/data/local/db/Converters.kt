package com.hanhyo.data.local.db

import androidx.room.TypeConverter
import com.hanhyo.domain.model.PushupType
import java.util.Date

class Converters {

    @TypeConverter
    fun fromPushupType(type: PushupType): String = type.name

    @TypeConverter
    fun toPushupType(name: String): PushupType = PushupType.valueOf(name)

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? = date?.time
}
