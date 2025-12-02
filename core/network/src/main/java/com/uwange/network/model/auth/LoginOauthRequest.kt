package com.uwange.network.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginOauthRequest(
    val providerName: String,
    val accessToken: String
)