package com.uwange.common

import kotlinx.coroutines.CancellationException

suspend inline fun <T, R> T.suspendRunCatching(crossinline block: suspend T.() -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (t: Throwable) {
        Result.failure(t)
    }
}