package com.example.allahs99names.presentation.trainings.match_pair

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.allahs99names.presentation.trainings.match_pair.TrainingMatchState.Content
import com.example.allahs99names.presentation.trainings.match_pair.TrainingMatchState.Nothing
import com.example.allahs99names.presentation.trainings.utils.TrainingErrorModal
import com.example.allahs99names.presentation.trainings.utils.TrainingSuccessfulModal
import com.example.allahs99names.ui.theme.Allahs99NamesTheme
import com.example.allahs99names.ui.utils.rippleClickable

@Composable
fun TrainingMatchScreen(goToNextTraining: () -> Unit) {
    val viewModel: TrainingMatchViewModel = hiltViewModel()
    val state = viewModel.state.collectAsState()

    when (val observedState = state.value) {
        is Content -> Content(observedState, viewModel, goToNextTraining)

        Nothing -> Unit
    }
}

@Composable
private fun Content(content: Content, viewModel: TrainingMatchViewModel, goToNextTraining: () -> Unit) {
    val isSelectedArabicOption = remember { mutableStateOf<FullBlessedNameEntity?>(null) }
    val isSelectedTranslationOption = remember { mutableStateOf<FullBlessedNameEntity?>(null) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp),
            text = stringResource(id = R.string.training_match_pair_title),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 20.sp
        )

        NameOptionsBlock(
            namesToGuess = content.namesToGuess,
            namesToGuessShuffled = content.namesToGuessShuffled,
            isSelectedArabicOption = isSelectedArabicOption,
            isSelectedTranslationOption = isSelectedTranslationOption
        ) { nameRecordingId ->
            viewModel.playSound(context, nameRecordingId)
        }

        if (content.isComplete == true) {
            TrainingSuccessfulModal(
                playSound = { soundId ->
                    viewModel.playSound(context, soundId)
                },
                onContinueClicked = {
                    viewModel.dropState()
                    goToNextTraining.invoke()
                }
            )
        }
        if (content.isComplete == false) {
            TrainingErrorModal(
                correctAnswer = null,
                playSound = { soundId ->
                    viewModel.playSound(context, soundId)
                },
                onContinueClicked = {
                    viewModel.dropState()
                    goToNextTraining.invoke()
                }
            )
        }
    }
}

@Composable
private fun NameOptionsBlock(
    namesToGuess: List<FullBlessedNameEntity>,
    namesToGuessShuffled: List<FullBlessedNameEntity>,
    isSelectedArabicOption: MutableState<FullBlessedNameEntity?>,
    isSelectedTranslationOption: MutableState<FullBlessedNameEntity?>,
    playSound: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        NameOptionsColumn(
            modifier = Modifier.weight(1f),
            namesToGuess = namesToGuess,
            isSelectedState = isSelectedArabicOption,
            isArabicVersion = true,
            playSound = playSound
        )
        NameOptionsColumn(
            modifier = Modifier.weight(1f),
            namesToGuess = namesToGuessShuffled,
            isArabicVersion = false,
            isSelectedState = isSelectedTranslationOption
        )
    }
}

@Composable
private fun NameOptionsColumn(
    modifier: Modifier,
    namesToGuess: List<FullBlessedNameEntity>,
    isSelectedState: MutableState<FullBlessedNameEntity?>,
    isArabicVersion: Boolean,
    playSound: ((Int) -> Unit)? = null
) {
    Column(
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.height(64.dp))
        namesToGuess.forEach {
            SingleOptionComponent(
                modifier = Modifier.weight(1f),
                fullNameEntity = it,
                isSelectedState = isSelectedState,
                isArabicVersion = isArabicVersion,
                playSound = playSound
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        Spacer(modifier = Modifier.height(64.dp))
    }
}

@Composable
private fun SingleOptionComponent(
    modifier: Modifier,
    fullNameEntity: FullBlessedNameEntity,
    isSelectedState: MutableState<FullBlessedNameEntity?>,
    isArabicVersion: Boolean,
    playSound: ((Int) -> Unit)? = null
) {
    val isSelected = remember { mutableStateOf(false) }

    LaunchedEffect(isSelectedState.value) {
        isSelected.value = isSelectedState.value == fullNameEntity
    }

    Box(
        modifier = modifier
            .padding(horizontal = 20.dp)
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
                isSelectedState.value = if (isSelectedState.value == fullNameEntity) null else fullNameEntity
                playSound?.invoke(fullNameEntity.nameRecordingId)
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(12.dp),
            text = if (isArabicVersion) {
                fullNameEntity.arabicVersion
            } else {
                fullNameEntity.russianVersion
            },
            color = if (isSelected.value) {
                MaterialTheme.colorScheme.primary
            } else {
                Color.Gray
            },
            fontSize = if (isArabicVersion) 28.sp else {
                20.sp
            },
            textAlign = TextAlign.Center,
            lineHeight = 28.sp
        )
    }
}

@Composable
@Preview
private fun TrainingMatchScreenPreview() {
    Allahs99NamesTheme {
        TrainingMatchScreen {}
    }
}