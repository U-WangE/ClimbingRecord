package com.uwange.network.source.configure

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfigureDataSource @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig,
    val json: Json
) {
    suspend inline fun <reified T> getReferenceType(key: String): T? {
        return json.decodeFromString<T>(getString(key, ""))
    }

    suspend fun getString(key: String, defaultValue: String): String =
        getValue(key)?.asString() ?: defaultValue

    suspend fun getValue(key: String): FirebaseRemoteConfigValue? =
        suspendCancellableCoroutine { continuation ->
            remoteConfig.
        }
}