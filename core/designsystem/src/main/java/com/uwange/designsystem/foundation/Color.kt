package com.uwange.designsystem.foundation

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

private val PrimaryDefault = Color(0xFFD0BCFF)
private val PrimaryMiddle = Color(0xFFCCC2DC)
private val PrimaryLight = Color(0xFFEFB8C8)

private val SubDefault = Color(0xFF6650a4)
private val SubMiddle = Color(0xFF625b71)
private val SubLight = Color(0xFF7D5260)

// https://pin.it/7LRvisiy3
private val Black = Color(0xFF0F0F0F)
private val Dark1 = Color(0xFF1C1C1C)
private val Dark2 = Color(0xFF3C3C3C)
private val Dark3 = Color(0xFF5A5A5A)
private val Dark4 = Color(0xFF7A7A7A)
private val Light1 = Color(0xFF9B9B9B)
private val Light2 = Color(0xFFBFBFBF)
private val Light3 = Color(0xFFDCDCDC)
private val White = Color(0xFFF4F4F4)

@Immutable
data class ClimbingRecordColors(
    val primaryDefault: Color = PrimaryDefault,
    val primaryMiddle: Color = PrimaryMiddle,
    val primaryLight: Color = PrimaryLight,
    val subDefault: Color = SubDefault,
    val subMiddle: Color = SubMiddle,
    val subLight: Color = SubLight,
    val black: Color = Black,
    val dark1: Color = Dark1,
    val dark2: Color = Dark2,
    val dark3: Color = Dark3,
    val dark4: Color = Dark4,
    val light1: Color = Light1,
    val light2: Color = Light2,
    val light3: Color = Light3,
    val white: Color = White
)