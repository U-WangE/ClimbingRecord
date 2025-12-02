package com.uwange.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.uwange.datastore.datasource.token.LocalTokenDataSource
import com.uwange.datastore.datasource.user.LocalUserDataSource
import com.uwange.domain.model.auth.OAuthProvider
import com.uwange.domain.model.user.UserRole
import com.uwange.domain.repository.AuthRepository
import com.uwange.network.source.auth.AuthDataSource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val localTokenDataSource: LocalTokenDataSource,
    private val localUserDataSource: LocalUserDataSource,
    private val firebaseAuth: FirebaseAuth
): AuthRepository {
    override suspend fun loginOauth(
        oAuthProvider: OAuthProvider,
        oauthCredential: String
    ): UserRole {
        val response = authDataSource.loginOauth(oAuthProvider, oauthCredential)

        coroutineScope {
            val accessTokenJob = launch {
                response.accessToken?.let { localTokenDataSource.setAccessToken(it) }
            }
            val userRoleJob = launch {
                response.role?.let { localUserDataSource.setUserRole(it) }
            }

            accessTokenJob.join()
            userRoleJob.join()
        }

        return UserRole.create(response.role)
    }

    override suspend fun logout() {
        coroutineScope {
            val clearTokenJob = launch { localTokenDataSource.clearToken() }

            clearTokenJob.join()

            firebaseAuth.signOut()
        }
    }

    override suspend fun checkTokenHealth() {
        val accessToken = localTokenDataSource.accessToken.first()

        if (accessToken.isBlank()) {
            throw IllegalArgumentException("저장되어 있는 토큰이 없습니다.")
        }

        authDataSource.checkTokenHealth(accessToken)
    }
}