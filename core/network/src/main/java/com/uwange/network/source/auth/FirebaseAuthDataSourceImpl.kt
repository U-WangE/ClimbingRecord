package com.uwange.network.source.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.uwange.domain.model.auth.OAuthProvider
import com.uwange.domain.model.auth.User
import javax.inject.Inject

class FirebaseAuthDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): AuthDataSource {
    override suspend fun loginOauth(
        provider: OAuthProvider,
        oauthToken: String
    ): User {
    }

    override suspend fun fetchUserFromFirestore(userId: String): User? {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserRoleInFirestore(userId: String, userRole: String) {
        TODO("Not yet implemented")
    }

    override suspend fun logout() {
        TODO("Not yet implemented")
    }

    override fun isLoggedIn(): Boolean {
        TODO("Not yet implemented")
    }
}