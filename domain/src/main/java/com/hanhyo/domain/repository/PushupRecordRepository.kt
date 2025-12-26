package com.hanhyo.domain.repository

import com.hanhyo.domain.model.PushupSession
import com.hanhyo.domain.model.PushupType
import kotlinx.coroutines.flow.Flow

interface PushupRecordRepository {
    suspend fun startSession(type: PushupType): Long
    suspend fun updateSession(sessionId: Long, count: Int)
    suspend fun endSession(sessionId: Long)
    fun observeSessions(): Flow<List<PushupSession>>
}
