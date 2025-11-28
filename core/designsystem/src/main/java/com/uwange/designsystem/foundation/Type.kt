package com.uwange.designsystem.foundation

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.uwange.climbingrecord.designsystem.R

// https://noonnu.cc/font_page/694)
private val PretendardRegular = FontFamily(
    Font(R.font.pretendard_regular, FontWeight.Normal)
)

private val PretendardSemiBold = FontFamily(
    Font(R.font.pretendard_semi_bold, FontWeight.SemiBold)
)

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
    val headingXLSB: TextStyle = textStyle(PretendardSemiBold, 28.sp, 40.sp),
    val headingLSB: TextStyle = textStyle(PretendardSemiBold, 24.sp, 32.sp),
    val headingMSB: TextStyle = textStyle(PretendardSemiBold, 20.sp, 24.sp),
    val headingSSB: TextStyle = textStyle(PretendardSemiBold, 18.sp, 22.sp),
    val bodyMSB: TextStyle = textStyle(PretendardSemiBold, 16.sp, 24.sp),
    val bodyMR: TextStyle = textStyle(PretendardRegular, 16.sp, 24.sp),
    val bodySSB: TextStyle = textStyle(PretendardSemiBold, 14.sp, 20.sp),
    val bodySR: TextStyle = textStyle(PretendardRegular, 14.sp, 20.sp)
)