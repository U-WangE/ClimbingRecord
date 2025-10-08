package com.uwange.designsystem

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

private val defaultLineHeightStyle = LineHeightStyle(
    alignment = LineHeightStyle.Alignment.Center,
    trim = LineHeightStyle.Trim.None
)

private fun textStyle(
    fontFamily: FontFamily,
    fontSize: TextUnit,
    lineHeight: TextUnit
) = TextStyle(
    fontFamily = fontFamily,
    fontSize = fontSize,
    lineHeight = lineHeight,
    lineHeightStyle = defaultLineHeightStyle
)


@Immutable
data class ClimbingRecordTypography(
    val headingXLSB: TextStyle = textStyle(FontFamily.Default, 28.sp, 40.sp)
)