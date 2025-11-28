package com.uwange.network.source.firebase

import com.uwange.domain.model.auth.OAuthProvider
import com.uwange.network.model.auth.LoginOauthResponse
import javax.inject.Inject

class FireStoreDataSourceImpl @Inject constructor(
): FirebaseAuthDataSource {
    override suspend fun loginOauth(
        oAuthProvider: OAuthProvider,
        oauthCredential: String
    ): LoginOauthResponse {
        TODO("Not yet implemented")
    }

}