package com.example.allahs99names.presentation.start_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.allahs99names.ui.theme.Allahs99NamesTheme

@Composable
fun StartScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = "This is the start screen"
        )
    }
}

@Composable
@Preview
private fun StartScreenPreview() {
    Allahs99NamesTheme {
        StartScreen()
    }
}