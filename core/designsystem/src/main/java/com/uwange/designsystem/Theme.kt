package com.uwange.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

private val LocalColors = staticCompositionLocalOf {
    ClimbingRecordColors()
}
private val LocalTypography = staticCompositionLocalOf {
    ClimbingRecordTypography()
}
@Composable
fun ClimbingRecordTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(content = content)
}

object ClimbingRecordTheme {
    val colors: ClimbingRecordColors
        @Composable
        get() = LocalColors.current
    val typography: ClimbingRecordTypography
        @Composable
        get() = LocalTypography.current
}