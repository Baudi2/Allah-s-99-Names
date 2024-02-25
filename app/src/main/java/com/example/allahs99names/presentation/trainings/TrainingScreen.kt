package com.example.allahs99names.presentation.trainings

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.allahs99names.presentation.trainings.utils.TrainingLeaveModal
import com.example.allahs99names.presentation.trainings.listen.TrainingHearOneScreen
import com.example.allahs99names.presentation.trainings.listen.TrainingHearTwoScreen
import com.example.allahs99names.presentation.trainings.match_pair.TrainingMatchScreen
import com.example.allahs99names.ui.theme.Allahs99NamesTheme

enum class TrainingTypes {
    HEAR_ONE,
    HEAR_TWO,
    MATCH_PAIR
}

@Composable
fun TrainingScreen(navController: NavController) {
    val currentTraining = remember { mutableStateOf(TrainingTypes.MATCH_PAIR) }
    val isLeaveModalDisplayed = remember { mutableStateOf(false) }

    BackHandler {
        isLeaveModalDisplayed.value = true
    }

    when (currentTraining.value) {
        TrainingTypes.MATCH_PAIR -> {
            TrainingMatchScreen {
                currentTraining.value = TrainingTypes.HEAR_ONE
            }
        }

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

    if (isLeaveModalDisplayed.value) {
        TrainingLeaveModal(
            onLeaveClicked = {
                navController.popBackStack()
                isLeaveModalDisplayed.value = false
            },
            onDismiss = {
                isLeaveModalDisplayed.value = false
            }
        )
    }
}

@Composable
@Preview
private fun TrainingScreenPreview() {
    Allahs99NamesTheme {
        TrainingScreen(NavController(LocalContext.current))
    }
}
