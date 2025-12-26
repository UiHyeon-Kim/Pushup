package com.hanhyo.presentation.ui.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hanhyo.domain.model.PushupSession
import com.hanhyo.domain.repository.PushupRecordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PushupRecordViewModel @Inject constructor(
    private val repository: PushupRecordRepository
) : ViewModel() {
    val sessions: StateFlow<List<PushupSession>> =
        repository.observeSessions()
            .catch { e ->
                Timber.tag("PushupRecordViewModel").e(e, "세션 조회 실패")
                emit(emptyList())
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )
}
