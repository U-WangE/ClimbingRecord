package com.uwange.presentation.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.uwange.debug.DebugDrawer
import com.uwange.domain.model.ForceUpdate

@Composable
fun ClimbingRecordApp(
    appState: ClimbingRecordAppState,
    forceUpdate: ForceUpdate?
) {
    val scope = rememberCoroutineScope()

    DebugDrawer(navController = appState.navController) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
        }
    }
}