package com.uwange.domain.repository

import com.uwange.domain.model.auth.OAuthProvider
import com.uwange.domain.model.auth.User
import com.uwange.domain.model.user.UserRole

interface AuthRepository {
    suspend fun loginOauth(
        oAuthProvider: OAuthProvider,
        oauthCredential: String
    ): UserRole

    suspend fun getCurrentUser(): User?
    suspend fun updateUserRole(userRole: UserRole)
    suspend fun logout()
    suspend fun isLoggedIn(): Boolean
}