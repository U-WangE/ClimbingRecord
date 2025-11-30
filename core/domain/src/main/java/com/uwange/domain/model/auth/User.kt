package com.uwange.domain.model.auth

import com.uwange.domain.model.user.UserRole

data class User(
    val userId: String,
    val socialId: String,
    val loginProvider: OAuthProvider,
    val userRole: UserRole,
)