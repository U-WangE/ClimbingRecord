package com.uwange.network.source.error

import android.util.Log
import javax.inject.Inject

class DebugErrorDataSourceImpl @Inject constructor() : ErrorDataSource {
    private var userId: String = ""

    override suspend fun logError(exception: Throwable) {
        Log.e(
            "DebugErrorDataSourceImpl",
            "userId : $userId, exception : ${exception.stackTraceToString()}"
        )
    }

    override suspend fun setUserId(userId: String) {
        this.userId = userId
    }

    override suspend fun clearUserId() {
        this.userId = ""
    }
}