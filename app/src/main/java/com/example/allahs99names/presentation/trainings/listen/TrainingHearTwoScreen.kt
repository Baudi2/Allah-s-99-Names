package com.example.allahs99names.presentation.trainings.listen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.allahs99names.R
import com.example.allahs99names.domain.model.FullBlessedNameEntity
import com.example.allahs99names.presentation.trainings.IsComplete
import com.example.allahs99names.presentation.trainings.SimpleTrainingViewModel
import com.example.allahs99names.presentation.trainings.utils.TrainingErrorModal
import com.example.allahs99names.presentation.trainings.utils.TrainingSuccessfulModal
import com.example.allahs99names.presentation.trainings.SimpleTrainingState.Content
import com.example.allahs99names.presentation.trainings.SimpleTrainingState.Nothing
import com.example.allahs99names.ui.components.ButtonComponent
import com.example.allahs99names.ui.components.ButtonState
import com.example.allahs99names.ui.theme.Allahs99NamesTheme
import com.example.allahs99names.ui.utils.rippleClickable

private const val OPTIONS_TO_GUESS = 3

@Composable
fun TrainingHearTwoScreen(isLastTraining: Boolean, goToNextTraining: (IsComplete) -> Unit) {
    val viewModel: SimpleTrainingViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        viewModel.loadContent(OPTIONS_TO_GUESS)
    }

    val state = viewModel.state.collectAsState()

    when (val observedState = state.value) {
        is Content -> {
            Content(observedState, viewModel, isLastTraining, goToNextTraining)
        }

        Nothing -> Unit
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(content: Content, viewModel: SimpleTrainingViewModel, isLastTraining: Boolean, goToNextTraining: (IsComplete) -> Unit) {
    val selectedName = remember { mutableStateOf<FullBlessedNameEntity?>(null) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp),
            text = stringResource(id = R.string.training_hear_two_title),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 20.sp
        )

        PlaySoundButton(content.nameToGuess.arabicVersion) {
            viewModel.playSound(context, content.nameToGuess.nameRecordingId)
        }

        NamesBlockComponent(content.namesOptions, selectedName)

        Spacer(modifier = Modifier.weight(1f))

        ButtonComponent(
            modifier = Modifier
                .padding(bottom = 26.dp),
            state = if (selectedName.value == null) ButtonState.DISABLED else ButtonState.ENABLED,
            text = if (selectedName.value == null) {
                stringResource(id = R.string.training_button_continue_disabled_text)
            } else {
                stringResource(id = R.string.training_button_continue_enabled_text)
            },
            onClick = {
                selectedName.value?.let {
                    viewModel.checkSelectedOption(it, content.nameToGuess)
                }
            }
        )
    }

    if (content.isCorrect == true) {
        TrainingSuccessfulModal(
            playSound = { soundId ->
                viewModel.playSound(context, soundId)
            },
            onContinueClicked = {
                viewModel.dropState()
                goToNextTraining.invoke(true)
            }
        )
    }
    if (content.isCorrect == false) {
        TrainingErrorModal(
            correctAnswer = content.nameToGuess.russianVersion,
            playSound = { soundId ->
                viewModel.playSound(context, soundId)
            },
            onContinueClicked = {
                if (isLastTraining) {
                    viewModel.reloadTraining(OPTIONS_TO_GUESS)
                    selectedName.value = null
                } else {
                    viewModel.dropState()
                    goToNextTraining.invoke(false)
                }
            }
        )
    }
}

@Composable
private fun PlaySoundButton(correctName: String, playNameRecording: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 74.dp)
            .padding(top = 84.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(12)
            )
            .border(
                border = BorderStroke(2.dp, Color.LightGray),
                shape = RoundedCornerShape(12)
            )
            .rippleClickable {
                playNameRecording.invoke()
            }
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(start = 30.dp, top = 44.dp, end = 30.dp, bottom = 44.dp),
            text = correctName,
            fontSize = 48.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            lineHeight = 58.sp
        )
        Icon(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(top = 12.dp, end = 18.dp, bottom = 18.dp)
                .size(34.dp),
            painter = painterResource(id = R.drawable.icon_play_name_recording),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun NamesBlockComponent(names: List<FullBlessedNameEntity>, selectedName: MutableState<FullBlessedNameEntity?>) {
    Spacer(modifier = Modifier.height(64.dp))
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        SingleNameComponent(names[0], selectedName)
        Spacer(modifier = Modifier.height(8.dp))
        SingleNameComponent(names[1], selectedName)
        Spacer(modifier = Modifier.height(8.dp))
        SingleNameComponent(names[2], selectedName)
    }
}

@Composable
private fun SingleNameComponent(fullNameEntity: FullBlessedNameEntity, isSelectedState: MutableState<FullBlessedNameEntity?>) {
    val isSelected = remember { mutableStateOf(false) }

    LaunchedEffect(isSelectedState.value) {
        isSelected.value = isSelectedState.value == fullNameEntity
    }

    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(12)
            )
            .border(
                border = BorderStroke(
                    width = 2.dp,
                    color = if (isSelected.value) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color.LightGray
                    }
                ),
                shape = RoundedCornerShape(12)
            )
            .rippleClickable {
                isSelectedState.value = if (isSelectedState.value == fullNameEntity) null else fullNameEntity
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(12.dp),
            text = fullNameEntity.russianVersion,
            color = if (isSelected.value) {
                MaterialTheme.colorScheme.primary
            } else {
                Color.Gray
            },
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            lineHeight = 28.sp
        )
    }
}

@Preview
@Composable
private fun TrainingHearTwoScreenPreview() {
    Allahs99NamesTheme {
        TrainingHearTwoScreen(false) {}
    }
}
