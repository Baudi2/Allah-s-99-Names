package com.example.allahs99names.presentation.trainings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.allahs99names.presentation.trainings.listen.TrainingHearOneScreen
import com.example.allahs99names.presentation.trainings.listen.TrainingHearTwoScreen
import com.example.allahs99names.ui.theme.Allahs99NamesTheme

enum class TrainingTypes {
    HEAR_ONE,
    HEAR_TWO
}

@Composable
fun TrainingScreen(navController: NavController) {
    val currentTraining = remember { mutableStateOf(TrainingTypes.HEAR_ONE) }

    when (currentTraining.value) {
        TrainingTypes.HEAR_ONE -> {
            TrainingHearOneScreen {
                currentTraining.value = TrainingTypes.HEAR_TWO
            }
        }
        TrainingTypes.HEAR_TWO -> {
            TrainingHearTwoScreen {
                navController.popBackStack()
            }
        }
    }
}

@Composable
@Preview
private fun TrainingScreenPreview() {
    Allahs99NamesTheme {
        TrainingScreen(NavController(LocalContext.current))
    }
}
