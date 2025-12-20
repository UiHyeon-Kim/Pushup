package com.hanhyo.domain.model

/**
 * 푸쉬업 상태
 * @property PushupState.Far 푸쉬업 중 멀어졌을 때
 * @property PushupState.Near 푸쉬업 중 가까워졌을 때
 * @property PushupState.Unknown 센서 상태를 알 수 없을 때
 */
sealed class PushupState {
    object Far : PushupState()
    object Near : PushupState()
    object Unknown : PushupState()
}
