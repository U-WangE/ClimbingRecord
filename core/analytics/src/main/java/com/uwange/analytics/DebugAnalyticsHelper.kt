package com.uwange.analytics

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class DebugAnalyticsHelper @Inject constructor(
    private var userId: String = ""
): AnalyticsHelper() {
    private val _stacked = MutableStateFlow<List<Triple<Long, String, AnalyticsEvent>>>(emptyList())
    val stackedAnalytics: StateFlow<List<Triple<Long, String, AnalyticsEvent>>> = _stacked.asStateFlow()

    override fun logEvent(event: AnalyticsEvent) {
        val timestamp = System.currentTimeMillis()
        Log.d("DebugAnalyticsHelper", "[$timestamp] userId=$userId event=$event")
        _stacked.update { it + Triple(timestamp, userId, event) }
    }

    override fun setUserId(userId: String?) {
        this.userId = userId?.let { "climbingrecord_android_$userId" } ?: ""
        Log.d("DebugAnalyticsHelper", "userId 호출 : ${this.userId}")
    }
}