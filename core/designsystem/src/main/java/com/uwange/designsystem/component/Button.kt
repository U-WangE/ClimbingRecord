package com.uwange.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uwange.climbingrecord.designsystem.R
import com.uwange.designsystem.foundation.ClimbingRecordTheme


@Composable
fun ClimbingRecordLoginButton(
    label: String,
    @DrawableRes imageId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    border: BorderStroke? = null,
    containerColor: Color = ClimbingRecordTheme.colors.primaryDefault,
    labelColor: Color = ClimbingRecordTheme.colors.black
) {
    var lastClickTime by remember { mutableLongStateOf(0L) }

    Button(
        onClick = {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime >= 2000L) {
                onClick()
                lastClickTime = currentTime
            }
        },
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
        border = border,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = ClimbingRecordTheme.colors.white,
            disabledContainerColor = ClimbingRecordTheme.colors.light1,
            disabledContentColor = ClimbingRecordTheme.colors.white,
        ),
        modifier = modifier
            .height(52.dp)
            .widthIn(min = 100.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(imageId),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
            )

            Text(
                text = label,
                style = ClimbingRecordTheme.typography.bodyMSB,
                color = labelColor,
            )
        }
    }
}


@Preview
@Composable
fun PreviewPieceLoginButton() {
    ClimbingRecordTheme {
        ClimbingRecordLoginButton(
            label = "Label",
            imageId = R.drawable.ic_alarm,
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )
    }
}