package com.hanhyo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hanhyo.data.local.entity.PushupSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PushupSessionDao {

    @Insert
    suspend fun insert(session: PushupSessionEntity): Long

    @Query("SELECT * FROM pushup_session ORDER BY startTime DESC")
    fun observeSession(): Flow<List<PushupSessionEntity>>
}
