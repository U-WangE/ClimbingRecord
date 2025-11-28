package com.uwange.data.repository

import com.uwange.domain.model.auth.OAuthProvider
import com.uwange.domain.model.user.UserRole
import com.uwange.domain.repository.AuthRepository
import com.uwange.network.source.firebase.FirebaseAuthDataSource
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: FirebaseAuthDataSource
): AuthRepository {
    override suspend fun loginOauth(
        oAuthProvider: OAuthProvider,
        oauthCredential: String
    ): UserRole {
        val response = authDataSource
    }

    override suspend fun logout() {
    }
}