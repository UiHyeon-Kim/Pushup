package com.hanhyo.data.repositoryimpl

import com.hanhyo.data.local.datasource.PushupLocalDataSource
import com.hanhyo.data.local.entity.PushupSessionEntity
import com.hanhyo.data.mapper.toDomain
import com.hanhyo.domain.model.PushupSession
import com.hanhyo.domain.model.PushupType
import com.hanhyo.domain.repository.PushupRecordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PushupRecordRepositoryImpl @Inject constructor(
    private val pushupLocalDataSource: PushupLocalDataSource
) : PushupRecordRepository {

    override suspend fun startSession(type: PushupType): Long {
        return pushupLocalDataSource.saveSession(
            PushupSessionEntity(
                type = type.name,
                startTime = System.currentTimeMillis(),
                endTime = null,
                totalCount = 0,
                isCompleted = false,
                maxConsecutive = 0,
                calories = 0,
            )
        )
    }

    override suspend fun updateSession(sessionId: Long, count: Int) {
        val session = pushupLocalDataSource.getSessionById(sessionId) ?: return

        val durationMin = (System.currentTimeMillis() - session.startTime) / MILLIS_PER_SECOND / SECONDS_PER_MINUTE
        val calories = (durationMin * CALORIES_PER_MINUTE).toInt()

        return pushupLocalDataSource.updateSession(
            session.copy(
                totalCount = session.totalCount + count,
                calories = calories,
            )
        )
    }

    override suspend fun endSession(sessionId: Long) {
        val session = pushupLocalDataSource.getSessionById(sessionId) ?: return

        pushupLocalDataSource.updateSession(
            session.copy(
                endTime = System.currentTimeMillis(),
                isCompleted = true,
            )
        )
    }

    override fun observeSessions(): Flow<List<PushupSession>> {
        return pushupLocalDataSource.observeSessions().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    companion object {
        private const val MILLIS_PER_SECOND = 1000f
        private const val SECONDS_PER_MINUTE = 60f
        private const val CALORIES_PER_MINUTE = 7.5
    }
}
