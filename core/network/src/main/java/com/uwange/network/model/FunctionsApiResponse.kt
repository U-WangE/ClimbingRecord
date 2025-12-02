package com.uwange.network.model

import kotlinx.serialization.Serializable

@Serializable
data class FunctionsResponse<T>(
    val status: String? = null,
    val message: String? = null,
    val data: T? = null,
    val error: FunctionsErrorResponse? = null // error 필드 추가
)

@Serializable
data class FunctionsErrorResponse(
    val code: String? = null,
    val message: String? = null,
    val errors: List<FunctionsErrorItem>? = emptyList()
)

@Serializable
data class FunctionsErrorItem(
    val field: String? = null,
    val message: String? = null
)

internal fun <T> FunctionsResponse<T>.unwrapData(): T {
    return data ?: Unit as T
}

const val UNKNOWN_INT = -1
const val UNKNOWN_STRING = "UNKNOWN"
