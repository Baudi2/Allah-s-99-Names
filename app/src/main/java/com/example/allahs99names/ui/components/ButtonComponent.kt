package com.example.allahs99names.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.allahs99names.ui.theme.Allahs99NamesTheme
import com.example.allahs99names.ui.utils.rippleClickable

enum class ButtonState {
    DISABLED,
    ENABLED,
    ERROR
}

@Composable
fun ButtonComponent(
    modifier: Modifier = Modifier,
    state: ButtonState,
    text: String,
    allCaps: Boolean = false,
    outsidePadding: PaddingValues = PaddingValues(horizontal = 20.dp),
    onClick: () -> Unit
) {
    val backgroundColor = when (state) {
        ButtonState.DISABLED -> Color.Gray
        ButtonState.ENABLED -> MaterialTheme.colorScheme.primary
        ButtonState.ERROR -> MaterialTheme.colorScheme.error
    }
    val textColor = when (state) {
        ButtonState.DISABLED -> Color.LightGray
        ButtonState.ENABLED -> MaterialTheme.colorScheme.onPrimary
        ButtonState.ERROR -> MaterialTheme.colorScheme.onPrimary
    }

    val buttonText = if (allCaps) text.uppercase() else text

    Box(
        modifier = modifier
            .padding(outsidePadding)
            .fillMaxWidth()
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .rippleClickable(enabled = state != ButtonState.DISABLED) {
                    onClick.invoke()
                },
            text = buttonText,
            color = textColor,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview
@Composable
private fun ButtonComponentPreview() {
    Allahs99NamesTheme {
        Column {
            ButtonComponent(
                state = ButtonState.DISABLED,
                text = "Continue",
                allCaps = true,
                onClick = {}
            )
            Spacer(modifier = Modifier.height(20.dp))
            ButtonComponent(
                state = ButtonState.ENABLED,
                text = "Check",
                onClick = {}
            )
            Spacer(modifier = Modifier.height(20.dp))
            ButtonComponent(
                state = ButtonState.ERROR,
                text = "Got it",
                onClick = {}
            )
        }
    }
}
