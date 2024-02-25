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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.example.allahs99names.ui.utils.applyIf
import com.example.allahs99names.ui.utils.rippleClickable
import kotlinx.coroutines.delay

@Composable
fun TrainingMatchScreen(goToNextTraining: () -> Unit) {
    val viewModel: TrainingMatchViewModel = hiltViewModel()
    val state = viewModel.state.collectAsState()

    when (val observedState = state.value) {
        is Content -> Content(observedState, viewModel, goToNextTraining)

        Nothing -> Unit
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(content: Content, viewModel: TrainingMatchViewModel, goToNextTraining: () -> Unit) {
    val isSelectedArabicOption = remember { mutableStateOf<FullBlessedNameEntity?>(null) }
    val isSelectedTranslationOption = remember { mutableStateOf<FullBlessedNameEntity?>(null) }
    val context = LocalContext.current

    LaunchedEffect(isSelectedArabicOption.value != null && isSelectedTranslationOption.value != null) {
        if (isSelectedArabicOption.value != null && isSelectedTranslationOption.value != null) {
            delay(150) // задержка для отработки анимации выбора парного элемента
            viewModel.matchPair(
                arabicVersion = isSelectedArabicOption.value!!,
                translationVersion = isSelectedTranslationOption.value!!
            )
        }
    }

    LaunchedEffect(content.guessedNames.contains(isSelectedArabicOption.value)) {
        isSelectedArabicOption.value = null
        isSelectedTranslationOption.value = null
    }

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
            guessedNamesList = content.guessedNames,
            isSelectedArabicOption = isSelectedArabicOption,
            isSelectedTranslationOption = isSelectedTranslationOption,
            isErrorModalDisplayed = content.isComplete == false
        ) { nameRecordingId ->
            viewModel.playSound(context, nameRecordingId)
        }

        if (content.isComplete == true) {
            TrainingSuccessfulModal(
                playSound = { soundId ->
                    viewModel.playSound(context, soundId, true)
                },
                onContinueClicked = {
                    viewModel.dropState()
                    goToNextTraining.invoke()
                }
            )
        }
        if (content.isComplete == false) {
            TrainingErrorModal(
                title = R.string.training_error_modal_match_pair_title,
                description = R.string.training_error_modal_match_pair_description,
                buttonText = R.string.training_error_modal_match_pair_button_text,
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                correctAnswer = null,
                playSound = { soundId ->
                    viewModel.playSound(context, soundId, true)
                },
                onContinueClicked = {
                    isSelectedArabicOption.value = null
                    isSelectedTranslationOption.value = null
                    viewModel.removeErrorModal()
                },
                onDismiss = {
                    isSelectedArabicOption.value = null
                    isSelectedTranslationOption.value = null
                    viewModel.removeErrorModal()
                }
            )
        }
    }
}

@Composable
private fun NameOptionsBlock(
    namesToGuess: List<FullBlessedNameEntity>,
    namesToGuessShuffled: List<FullBlessedNameEntity>,
    guessedNamesList: Set<FullBlessedNameEntity>,
    isSelectedArabicOption: MutableState<FullBlessedNameEntity?>,
    isSelectedTranslationOption: MutableState<FullBlessedNameEntity?>,
    isErrorModalDisplayed: Boolean,
    playSound: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        NameOptionsColumn(
            modifier = Modifier.weight(1f),
            namesToGuess = namesToGuess,
            guessedNamesList = guessedNamesList,
            isSelectedState = isSelectedArabicOption,
            isArabicVersion = true,
            isErrorModalDisplayed = isErrorModalDisplayed,
            playSound = playSound
        )
        NameOptionsColumn(
            modifier = Modifier.weight(1f),
            namesToGuess = namesToGuessShuffled,
            guessedNamesList = guessedNamesList,
            isArabicVersion = false,
            isErrorModalDisplayed = isErrorModalDisplayed,
            isSelectedState = isSelectedTranslationOption
        )
    }
}

@Composable
private fun NameOptionsColumn(
    modifier: Modifier,
    namesToGuess: List<FullBlessedNameEntity>,
    guessedNamesList: Set<FullBlessedNameEntity>,
    isSelectedState: MutableState<FullBlessedNameEntity?>,
    isArabicVersion: Boolean,
    isErrorModalDisplayed: Boolean,
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
                isDisabled = it in guessedNamesList,
                isSelectedState = isSelectedState,
                isArabicVersion = isArabicVersion,
                isErrorModalDisplayed = isErrorModalDisplayed,
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
    isDisabled: Boolean,
    isSelectedState: MutableState<FullBlessedNameEntity?>,
    isArabicVersion: Boolean,
    isErrorModalDisplayed: Boolean,
    playSound: ((Int) -> Unit)? = null
) {
    val isSelected = remember { mutableStateOf(false) }

    LaunchedEffect(isSelectedState.value) {
        isSelected.value = isSelectedState.value == fullNameEntity
    }

    val boxBorderColor = when {
        isErrorModalDisplayed && isSelected.value -> MaterialTheme.colorScheme.error
        isDisabled -> Color.LightGray
        isSelected.value -> MaterialTheme.colorScheme.primary
        !isSelected.value -> Color.Gray
        else -> Color.LightGray
    }

    val textColor = when {
        isErrorModalDisplayed && isSelected.value -> MaterialTheme.colorScheme.error
        isDisabled -> Color.LightGray
        isSelected.value -> MaterialTheme.colorScheme.primary
        !isSelected.value -> Color.Gray
        else -> Color.LightGray
    }

    Box(
        modifier = modifier
            .applyIf(isArabicVersion) {
                padding(start = 20.dp, end = 8.dp)
            }
            .applyIf(isArabicVersion.not()) {
                padding(end = 20.dp, start = 8.dp)
            }
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(12)
            )
            .border(
                border = BorderStroke(
                    width = 2.dp,
                    color = boxBorderColor
                ),
                shape = RoundedCornerShape(12)
            )
            .rippleClickable(enabled = isDisabled.not()) {
                isSelectedState.value = if (isSelectedState.value == fullNameEntity) null else fullNameEntity
                if (isSelected.value.not()) {
                    playSound?.invoke(fullNameEntity.nameRecordingId)
                }
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
            color = textColor,
            fontSize = if (isArabicVersion) {
                28.sp
            } else {
                18.sp
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
