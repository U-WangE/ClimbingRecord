package com.uwange.data.repository

import com.uwange.datastore.datasource.token.LocalTokenDataSource
import com.uwange.domain.model.auth.OAuthProvider
import com.uwange.domain.model.user.UserRole
import com.uwange.domain.repository.AuthRepository
import com.uwange.network.source.auth.AuthDataSource
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val localTokenDataSource: LocalTokenDataSource
): AuthRepository {
    override suspend fun loginOauth(
        oAuthProvider: OAuthProvider,
        oauthCredential: String
    ): UserRole {

        TODO("Not yet implemented")
    }

    override suspend fun logout() {
        TODO("Not yet implemented")
    }

    override suspend fun checkTokenHealth() {
        val accessToken = localTokenDataSource.accessToken.first()

        if (accessToken.isBlank()) {

        }

        authDataSource.checkTokenHealth(accessToken)
    }
}