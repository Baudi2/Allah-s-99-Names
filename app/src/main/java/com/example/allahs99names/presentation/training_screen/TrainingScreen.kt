package com.example.allahs99names.presentation.training_screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.allahs99names.presentation.theme.Allahs99NamesTheme

@Composable
fun TrainingScreen() {
    Text(
        text = "Welcome to the app"
    )
}

@Composable
@Preview
private fun TrainingScreenPreview() {
    Allahs99NamesTheme {
        TrainingScreen()
    }
}