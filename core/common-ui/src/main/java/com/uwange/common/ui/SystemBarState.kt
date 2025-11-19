package com.uwange.common.ui

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SystemBarState {
    private val _statusBarColor = MutableStateFlow(Color.Transparent)
    val statusBarColor: StateFlow<Color> = _statusBarColor.asStateFlow()

    private val _navigationBarColor = MutableStateFlow(Color.Transparent)
    val navigationBarColor: StateFlow<Color> = _navigationBarColor.asStateFlow()

    fun setStatusBarColor(color: Color) {
        _statusBarColor.value = color
    }

    fun setNavigationBarColor(color: Color) {
        _navigationBarColor.value = color
    }
}

val LocalSystemBarState = compositionLocalOf {
    SystemBarState()
}

@Composable
fun StatusBarColor(color: Color) {
    val view = LocalView.current
    val useDarkIcons = color.luminance() > 0.5f || color == Color.Transparent

    LaunchedEffect(color) {
        if (!view.isInEditMode) {
            val window = (view.context as Activity).window
            window.statusBarColor = color.toArgb()
            delay(200L)
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                useDarkIcons
        }
    }
}

@Composable
fun NavigationBarColor(color: Color) {
    val view = LocalView.current
    val useDarkIcons = color.luminance() > 0.5f || color == Color.Transparent

    LaunchedEffect(color) {
        if (!view.isInEditMode) {
            val window = (view.context as Activity).window
            window.navigationBarColor = color.toArgb()
            delay(200L)
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                useDarkIcons
        }
    }
}