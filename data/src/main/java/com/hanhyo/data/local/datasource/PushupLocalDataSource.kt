package com.hanhyo.data.local.datasource

import com.hanhyo.data.local.dao.PushupSessionDao
import com.hanhyo.data.local.entity.PushupSessionEntity
import javax.inject.Inject

class PushupLocalDataSource @Inject constructor(
    private val pushupSessionDao: PushupSessionDao
) {
    suspend fun saveSession(session: PushupSessionEntity): Long = pushupSessionDao.insert(session)

    suspend fun updateSession(session: PushupSessionEntity) = pushupSessionDao.update(session)

    suspend fun getSessionById(id: Long): PushupSessionEntity? = pushupSessionDao.getSessionById(id)

    fun observeSessions() = pushupSessionDao.observeSessions()
}
