package com.uwange.auth.graph.login

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.user.UserApiClient
import com.uwange.climbingrecord.designsystem.R
import com.uwange.designsystem.component.ClimbingRecordLoginButton

@Composable
internal fun LoginRoute(
    viewModel: LoginViewModel = hiltViewModel()
) {
    BackHandler {

    }

    LaunchedEffect(viewModel) {

    }

    LoginScreen(
        loginKakao = {}
    )
}

@Composable
fun LoginScreen(
    loginKakao: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        ClimbingRecordLoginButton(
            label = stringResource(R.string.kakao_login),
            imageId = R.drawable.ic_kakao_login,
            containerColor = Color(0xFFFEE500),
            onClick = loginKakao,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )
    }
}

private fun loginKakao(
    context: Context,
    onSuccess: (String) -> Unit,
    onFailure: (Throwable) -> Unit
) {
    val callBack: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null && error !is ClientError) {
            onFailure(error)
        } else if (token != null) {
            onSuccess(token.accessToken)
        }
    }

    UserApiClient.instance.apply {
        if (isKakaoTalkLoginAvailable(context)) {
            // 카카오톡 로그인 (앱)
            loginWithKakaoTalk(context, callback = callBack)
        } else {
            // 카카오 계정 로그인 (웹)
            loginWithKakaoAccount(context, callback = callBack)
        }
    }
}