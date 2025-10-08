package com.uwange.designsystem

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

private val PrimaryDefault = Color(0xFFD0BCFF)
private val PrimaryMiddle = Color(0xFFCCC2DC)
private val PrimaryLight = Color(0xFFEFB8C8)

private val SubDefault = Color(0xFF6650a4)
private val SubMiddle = Color(0xFF625b71)
private val SubLight = Color(0xFF7D5260)

@Immutable
data class ClimbingRecordColors(
    val primaryDefault: Color = PrimaryDefault,
    val primaryMiddle: Color = PrimaryMiddle,
    val primaryLight: Color = PrimaryLight,
    val subDefault: Color = SubDefault,
    val subMiddle: Color = SubMiddle,
    val subLight: Color = SubLight
)