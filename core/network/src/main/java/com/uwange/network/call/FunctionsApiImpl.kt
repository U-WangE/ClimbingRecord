package com.uwange.network.call

import com.google.firebase.functions.FirebaseFunctions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uwange.network.model.FunctionsErrorResponse
import com.uwange.network.model.FunctionsResponse
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FunctionsApiImpl @Inject constructor(
    private val functions: FirebaseFunctions,
    private val gson: Gson
) : FunctionsApi {
    override suspend fun <T : Any, R : Any> call(
        functionName: String,
        requestData: R,
        responseClass: Class<T>
    ): FunctionsResponse<T> {
        return try {
            val result = functions.getHttpsCallable(functionName)
                .call(requestData)
                .await()

            val json = gson.toJson(result.getData())
            val type = TypeToken
                .getParameterized(FunctionsResponse::class.java, responseClass).type
            val responseWrapper = gson.fromJson<FunctionsResponse<T>>(json, type)

            responseWrapper
        } catch (e: Exception) {
            FunctionsResponse(
                data = null,
                status = "FAIL",
                message = e.message,
                error = FunctionsErrorResponse(
                    code = "EXCEPTION",
                    message = e.message
                )
            )
        }
    }
}