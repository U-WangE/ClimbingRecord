package com.uwange.network.source.auth

import com.uwange.domain.model.auth.OAuthProvider
import com.uwange.domain.model.auth.User

interface AuthDataSource {
    suspend fun loginOauth(
        provider: OAuthProvider,
        oauthToken: String
    ): User
    suspend fun fetchUserFromFirestore(userId: String): User?
    suspend fun updateUserRoleInFirestore(userId: String, userRole: String)
    suspend fun logout()
    fun isLoggedIn(): Boolean
}