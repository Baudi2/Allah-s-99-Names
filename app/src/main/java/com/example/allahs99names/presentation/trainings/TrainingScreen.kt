package com.example.allahs99names.presentation.trainings

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.allahs99names.presentation.trainings.hear_one.TrainingHearOneScreen
import com.example.allahs99names.ui.theme.Allahs99NamesTheme

@Composable
fun TrainingScreen() {
    // тут потом будет рандомное оперделение первой тренировки
    TrainingHearOneScreen()
}

@Composable
@Preview
private fun TrainingScreenPreview() {
    Allahs99NamesTheme {
        TrainingScreen()
    }
}
