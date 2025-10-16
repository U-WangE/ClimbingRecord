package com.uwange.auth.graph.login.contract

import com.uwange.ui.base.UiState


data class LoginState(
    val isLoading: Boolean = false,
) : UiState