package com.uwange.auth.graph.login

import androidx.lifecycle.viewModelScope
import com.uwange.analytics.AnalyticsHelper
import com.uwange.auth.graph.login.contract.LoginIntent
import com.uwange.auth.graph.login.contract.LoginSideEffect
import com.uwange.auth.graph.login.contract.LoginState
import com.uwange.common.base.BaseViewModel
import com.uwange.common.suspendRunCatching
import com.uwange.domain.model.auth.OAuthProvider
import com.uwange.domain.model.error.ErrorHelper
import com.uwange.domain.model.user.UserRole.BANNED
import com.uwange.domain.model.user.UserRole.NONE
import com.uwange.domain.model.user.UserRole.PENDING
import com.uwange.domain.model.user.UserRole.REGISTER
import com.uwange.domain.model.user.UserRole.USER
import com.uwange.domain.repository.AuthRepository
import com.uwange.navigation.AuthGraph
import com.uwange.navigation.NavigationEvent
import com.uwange.navigation.NavigationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val navigationHelper: NavigationHelper,
    private val errorHelper: ErrorHelper,
    private val analyticsHelper: AnalyticsHelper
) : BaseViewModel<LoginState, LoginIntent>(LoginState()) {

    private val _sideEffect = Channel<LoginSideEffect>(BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    override suspend fun processIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.LoginOAuth -> {
                setState { copy(isLoading = true) }

                when (intent.oAuthProvider) {
                    OAuthProvider.KAKAO -> _sideEffect.send(LoginSideEffect.LoginKakao)
                }
            }
        }
    }

    internal fun loginOAuth(oAuthProvider: OAuthProvider, token: String) = viewModelScope.launch {
        suspendRunCatching {
            authRepository.loginOauth(
                oAuthProvider,
                token
            )
        }.onSuccess { userRole ->
//            analyticsHelper.setUserId()
//            errorHelper.setUserId()

            when (userRole) {
                REGISTER -> navigationHelper.navigate(
                    NavigationEvent.To(
                        route = AuthGraph.SignUpRout,
                        popUpTo = true
                    )
                )
                PENDING, USER -> {}
                NONE -> navigationHelper.navigate(
                    NavigationEvent.To(
                        route = TODO(""),
                        popUpTo = true
                    )
                )
                BANNED -> {}
            }
        }.onFailure { errorHelper.sendError(it) }
            .also { setState { copy(isLoading = false) } }
    }
}