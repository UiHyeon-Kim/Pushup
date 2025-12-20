package com.hanhyo.data.local.db

import androidx.room.TypeConverter
import com.hanhyo.domain.model.PushupType
import java.util.Date

class Converters {

    @TypeConverter
    fun fromPushupType(type: PushupType): String = type.name

    /*
        예외 처리 이유
        1. DB 스키마 변경 시 - 특정 항목을 지우거나 이름을 변경했는데, DB 에 여전히 이름이 남아 있는 경우
        2. 실수로 DB 에 정의되지 않은 문자열이 저장되었을 경우, 로드 중 크래시 발생할 수 있음
     */
    @TypeConverter
    fun toPushupType(name: String): PushupType = try {
        PushupType.valueOf(name)
    } catch (_: IllegalArgumentException) {
        PushupType.UNKNOWN
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? = date?.time
}
