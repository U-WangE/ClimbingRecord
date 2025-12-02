package com.uwange.network.source.auth

import com.uwange.domain.model.auth.OAuthProvider
import com.uwange.domain.model.auth.User
import com.uwange.network.model.auth.LoginOauthResponse

interface AuthDataSource {
    suspend fun loginOauth(
        provider: OAuthProvider,
        accessToken: String
    ): LoginOauthResponse

    suspend fun checkTokenHealth(accessToken: String)
}