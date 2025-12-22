package com.hanhyo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.hanhyo.data.local.entity.PushupSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PushupSessionDao {

    @Insert
    suspend fun insert(session: PushupSessionEntity): Long

    @Update
    suspend fun update(session: PushupSessionEntity)

    @Query("SELECT * FROM pushup_session WHERE id = :id")
    suspend fun getSessionById(id: Long): PushupSessionEntity?

    @Query("SELECT * FROM pushup_session ORDER BY startTime DESC")
    fun observeSessions(): Flow<List<PushupSessionEntity>>
}
