package com.uwange.auth.graph.login

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.uwange.climbingrecord.designsystem.R
import com.uwange.designsystem.component.ClimbingRecordLoginButton

@Composable
internal fun LoginRoute() {
    BackHandler {

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
            imageId = ,
            containerColor = Color(0xFFFEE500),
        )
    }
}