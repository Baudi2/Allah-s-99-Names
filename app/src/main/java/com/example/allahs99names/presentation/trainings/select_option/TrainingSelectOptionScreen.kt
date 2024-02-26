package com.example.allahs99names.presentation.trainings.select_option

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.allahs99names.R
import com.example.allahs99names.domain.model.FullBlessedNameEntity
import com.example.allahs99names.presentation.trainings.IsComplete
import com.example.allahs99names.presentation.trainings.SimpleTrainingState.Content
import com.example.allahs99names.presentation.trainings.SimpleTrainingState.Nothing
import com.example.allahs99names.presentation.trainings.SimpleTrainingViewModel
import com.example.allahs99names.presentation.trainings.utils.TrainingErrorModal
import com.example.allahs99names.presentation.trainings.utils.TrainingSuccessfulModal
import com.example.allahs99names.ui.components.ButtonComponent
import com.example.allahs99names.ui.components.ButtonState
import com.example.allahs99names.ui.theme.Allahs99NamesTheme
import com.example.allahs99names.ui.utils.rippleClickable

private const val OPTIONS_TO_GUESS = 4

@Composable
fun TrainingSelectOptionScreen(isLastTraining: Boolean, goToNextTraining: (IsComplete) -> Unit) {
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
            text = stringResource(id = R.string.training_select_option_title, content.nameToGuess.transliteration),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 20.sp
        )

        NamesBlockComponent(
            modifier = Modifier.weight(1f),
            names = content.namesOptions,
            selectedName = selectedName
        ) { nameRecordingId ->
            viewModel.playSound(context, nameRecordingId)
        }

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
            correctAnswer = content.nameToGuess.arabicVersion,
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
private fun NamesBlockComponent(
    modifier: Modifier,
    names: List<FullBlessedNameEntity>,
    selectedName: MutableState<FullBlessedNameEntity?>,
    playSound: (Int) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        NamesBlockColumn(
            modifier = Modifier.weight(1f),
            singleNamePaddingValues = PaddingValues(start = 12.dp, end = 6.dp),
            names = Pair(names[0], names[1]),
            selectedName = selectedName,
            playSound = playSound
        )
        NamesBlockColumn(
            modifier = Modifier.weight(1f),
            singleNamePaddingValues = PaddingValues(start = 6.dp, end = 12.dp),
            names = Pair(names[2], names[3]),
            selectedName = selectedName,
            playSound = playSound
        )
    }
}

@Composable
private fun NamesBlockColumn(
    modifier: Modifier,
    singleNamePaddingValues: PaddingValues,
    names: Pair<FullBlessedNameEntity, FullBlessedNameEntity>,
    selectedName: MutableState<FullBlessedNameEntity?>,
    playSound: (Int) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        SingleNameComponent(
            modifier = Modifier
                .weight(1f)
                .padding(singleNamePaddingValues),
            fullNameEntity = names.first,
            isSelectedState = selectedName,
            playSound = playSound
        )
        Spacer(modifier = Modifier.height(6.dp))
        SingleNameComponent(
            modifier = Modifier
                .weight(1f)
                .padding(singleNamePaddingValues),
            fullNameEntity = names.second,
            isSelectedState = selectedName,
            playSound = playSound
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun SingleNameComponent(
    modifier: Modifier = Modifier,
    fullNameEntity: FullBlessedNameEntity,
    isSelectedState: MutableState<FullBlessedNameEntity?>,
    playSound: (Int) -> Unit
) {
    val isSelected = remember { mutableStateOf(false) }

    LaunchedEffect(isSelectedState.value) {
        isSelected.value = isSelectedState.value == fullNameEntity
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
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
                if (isSelectedState.value != fullNameEntity) {
                    isSelectedState.value = fullNameEntity
                }
                playSound.invoke(fullNameEntity.nameRecordingId)
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(12.dp),
            text = fullNameEntity.arabicVersion,
            color = if (isSelected.value) {
                MaterialTheme.colorScheme.primary
            } else {
                Color.Gray
            },
            fontSize = 38.sp,
            textAlign = TextAlign.Center,
            lineHeight = 48.sp
        )
    }
}

@Preview
@Composable
private fun TrainingSelectOptionScreenPreview() {
    Allahs99NamesTheme {
        TrainingSelectOptionScreen(false) {}
    }
}
