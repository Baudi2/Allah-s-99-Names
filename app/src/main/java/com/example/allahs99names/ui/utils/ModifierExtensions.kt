package com.example.allahs99names.ui.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color

fun Modifier.rippleClickable(color: Color = Color.Gray, enabled: Boolean = true, onClick: () -> Unit): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    this
        .clickable(
            interactionSource = interactionSource,
            indication = rememberRipple(bounded = true, color = color),
            onClick = onClick,
            enabled = enabled
        )
}
