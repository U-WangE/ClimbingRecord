package com.uwange.network.source.auth

import com.uwange.domain.model.auth.OAuthProvider
import com.uwange.network.call.FunctionsApi
import com.uwange.network.model.auth.LoginOauthRequest
import com.uwange.network.model.auth.LoginOauthResponse
import com.uwange.network.model.unwrapData
import javax.inject.Inject

class FirebaseAuthDataSourceImpl @Inject constructor(
    private val functionsApi: FunctionsApi
): AuthDataSource {
    override suspend fun loginOauth(
        provider: OAuthProvider,
        accessToken: String
    ): LoginOauthResponse = functionsApi.call(
        "loginOauth",
        LoginOauthRequest(
            providerName = provider.apiValue,
            accessToken = accessToken
        ),
        LoginOauthResponse::class.java
    ).unwrapData()

    override suspend fun checkTokenHealth(accessToken: String) {
        TODO("Not yet implemented")
    }

}