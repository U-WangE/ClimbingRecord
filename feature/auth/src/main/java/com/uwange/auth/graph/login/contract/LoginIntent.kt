package com.uwange.auth.graph.login.contract

import com.uwange.common.base.UiIntent
import com.uwange.domain.model.OAuthProvider

sealed class LoginIntent : UiIntent {
    data class LoginOAuth(val oAuthProvider: OAuthProvider) : LoginIntent()
}