package com.example.allahs99names.presentation.trainings

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.allahs99names.presentation.trainings.listen.TrainingHearOneScreen
import com.example.allahs99names.presentation.trainings.listen.TrainingHearTwoScreen
import com.example.allahs99names.presentation.trainings.match_pair.TrainingMatchScreen
import com.example.allahs99names.presentation.trainings.select_option.TrainingSelectOptionScreen
import com.example.allahs99names.presentation.trainings.utils.TrainingLeaveModal
import com.example.allahs99names.ui.theme.Allahs99NamesTheme

typealias IsComplete = Boolean

enum class TrainingTypes {
    HEAR_ONE,
    HEAR_TWO,
    MATCH_PAIR,
    SELECT_OPTION
}

@Composable
fun TrainingScreen(navController: NavController) {
    val allTrainings = remember {
        mutableListOf(
            TrainingTypes.HEAR_ONE,
            TrainingTypes.HEAR_TWO,
            TrainingTypes.MATCH_PAIR,
            TrainingTypes.SELECT_OPTION
        ).asSequence()
            .shuffled()
            .toMutableList()
    }
    val allTrainingsSize = remember { allTrainings.size.inc() }
    val currentTraining = remember { mutableStateOf(allTrainings.first()) }
    val isLeaveModalDisplayed = remember { mutableStateOf(false) }
    val currentStep = remember { mutableIntStateOf(0) }
    val currentProgress = remember { mutableIntStateOf(1) }

    BackHandler {
        isLeaveModalDisplayed.value = true
    }

    LaunchedEffect(currentStep.intValue) {
        if (currentProgress.intValue >= allTrainingsSize) {
            navController.popBackStack()
        } else {
            currentTraining.value = allTrainings[currentStep.intValue]
        }
    }

    Column {
        TrainingStepProgress(
            modifier = Modifier.wrapContentHeight(),
            steps = allTrainingsSize,
            currentProgress = currentProgress.intValue
        ) {
            isLeaveModalDisplayed.value = true
        }

        when (currentTraining.value) {
            TrainingTypes.SELECT_OPTION -> {
                val isLastTraining = (currentProgress.intValue + 1) >= allTrainingsSize
                TrainingSelectOptionScreen(isLastTraining) { isComplete ->
                    if (isComplete) {
                        currentProgress.intValue = currentProgress.intValue + 1
                    } else {
                        allTrainings.add(TrainingTypes.SELECT_OPTION)
                    }
                    currentStep.intValue = currentStep.intValue + 1
                }
            }

            TrainingTypes.MATCH_PAIR -> {
                TrainingMatchScreen { isComplete ->
                    if (isComplete) {
                        currentProgress.intValue = currentProgress.intValue + 1
                    } else {
                        allTrainings.add(TrainingTypes.MATCH_PAIR)
                    }
                    currentStep.intValue = currentStep.intValue.plus(1)
                }
            }

            TrainingTypes.HEAR_ONE -> {
                val isLastTraining = (currentProgress.intValue + 1) >= allTrainingsSize
                TrainingHearOneScreen(isLastTraining) { isComplete ->
                    if (isComplete) {
                        currentProgress.intValue = currentProgress.intValue + 1
                    } else {
                        allTrainings.add(TrainingTypes.HEAR_ONE)
                    }
                    currentStep.intValue = currentStep.intValue.plus(1)
                }
            }

            TrainingTypes.HEAR_TWO -> {
                val isLastTraining = (currentProgress.intValue + 1) >= allTrainingsSize
                TrainingHearTwoScreen(isLastTraining) { isComplete ->
                    if (isComplete) {
                        currentProgress.intValue = currentProgress.intValue + 1
                    } else {
                        allTrainings.add(TrainingTypes.HEAR_TWO)
                    }
                    currentStep.intValue = currentStep.intValue.plus(1)
                }
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
fun TrainingStepProgress(modifier: Modifier = Modifier, steps: Int, currentProgress: Int, onLeaveClicked: () -> Unit) {
    val progress = remember { Animatable(1f / steps) }

    val stepProgress = (currentProgress.toFloat() / steps).coerceAtMost(1f)

    LaunchedEffect(currentProgress) {
        progress.animateTo(
            targetValue = stepProgress,
            animationSpec = tween(
                durationMillis = 1200,
                easing = FastOutSlowInEasing
            )
        )
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onLeaveClicked) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        Spacer(Modifier.width(12.dp))
        LinearProgressIndicator(
            progress = {
                progress.value
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 20.dp)
                .height(8.dp),
            strokeCap = StrokeCap.Round
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
