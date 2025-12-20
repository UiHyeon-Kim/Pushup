package com.hanhyo.domain.repository

import com.hanhyo.domain.model.PushupSession

interface PushupSessionRepository {
    suspend fun saveSession(session: PushupSession): Long
}
