package com.uwange.presentation.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.uwange.debug.DebugDrawer
import com.uwange.designsystem.foundation.ClimbingRecordTheme
import com.uwange.domain.model.configure.ForceUpdate
import com.uwange.presentation.navigation.AppNavHost

@Composable
fun ClimbingRecordApp(
    appState: ClimbingRecordAppState,
    forceUpdate: ForceUpdate?
) {
    val scope = rememberCoroutineScope()

    DebugDrawer(navController = appState.navController) {
        Scaffold(
            containerColor = ClimbingRecordTheme.colors.white
        ) { innerPadding ->
            val topPadding by animateDpAsState(
                targetValue = innerPadding.calculateTopPadding(),
                label = "topPadding",
            )
            val bottomPadding by animateDpAsState(
                targetValue = innerPadding.calculateBottomPadding(),
                label = "bottomPadding"
            )
            val contentModifier = Modifier.padding(
                top = topPadding,
                bottom = bottomPadding
            )

            AppNavHost(
                navController = appState.navController,
                modifier = contentModifier
            )
        }
    }
}