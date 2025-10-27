package com.uwange.auth.graph.login

import androidx.lifecycle.ViewModel
import com.uwange.auth.graph.login.contract.LoginIntent
import com.uwange.auth.graph.login.contract.LoginSideEffect
import com.uwange.auth.graph.login.contract.LoginState
import com.uwange.common.base.BaseViewModel
import com.uwange.domain.model.OAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(

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


}