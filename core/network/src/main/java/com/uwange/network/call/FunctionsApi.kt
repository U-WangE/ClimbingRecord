package com.uwange.network.call

import com.uwange.network.model.FunctionsResponse

interface FunctionsApi {
    suspend fun <T : Any, R : Any> call(
        functionName: String,
        requestData: R,
        responseClass: Class<T>
    ): FunctionsResponse<T>
}