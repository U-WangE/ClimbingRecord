package com.uwange.data.repository

import com.uwange.domain.model.auth.OAuthProvider
import com.uwange.domain.model.auth.User
import com.uwange.domain.model.user.UserRole
import com.uwange.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
//    private val authDataSource: FirebaseAuthDataSource
): AuthRepository {
    override suspend fun loginOauth(
        oAuthProvider: OAuthProvider,
        oauthCredential: String
    ): UserRole {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrentUser(): User? {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserRole(userRole: UserRole) {
        TODO("Not yet implemented")
    }

    override suspend fun logout() {
    }

    override suspend fun isLoggedIn(): Boolean {
        TODO("Not yet implemented")
    }
}