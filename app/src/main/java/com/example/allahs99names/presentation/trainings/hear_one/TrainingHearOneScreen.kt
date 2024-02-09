package com.example.allahs99names.presentation.trainings.hear_one

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.allahs99names.presentation.trainings.components.TrainingErrorModal
import com.example.allahs99names.presentation.trainings.components.TrainingSuccessfulModal
import com.example.allahs99names.presentation.trainings.hear_one.TrainingOneHearState.Content
import com.example.allahs99names.presentation.trainings.hear_one.TrainingOneHearState.Nothing
import com.example.allahs99names.ui.components.ButtonComponent
import com.example.allahs99names.ui.components.ButtonState
import com.example.allahs99names.ui.theme.Allahs99NamesTheme
import com.example.allahs99names.ui.utils.rippleClickable

@Composable
fun TrainingHearOneScreen() {
    val viewModel: TrainingHearOneViewModel = hiltViewModel()
    val state = viewModel.state.collectAsState()

    when (val observedState = state.value) {
        is Content -> {
            Content(observedState, viewModel)
        }

        Nothing -> Unit
    }
}

@Composable
private fun Content(content: Content, viewModel: TrainingHearOneViewModel) {
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
            text = stringResource(id = R.string.training_hear_one_title),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 20.sp
        )

        PlayCorrectNameRecordingButtonComponent {
            viewModel.playSound(context, content.nameToGuess.nameRecordingId)
        }

        NamesToGuessBlockComponent(content.namesOptions, selectedName)

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
            }
        )
    }
}

@Composable
private fun PlayCorrectNameRecordingButtonComponent(playNameRecording: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .padding(top = 100.dp)
                .width(128.dp)
                .height(120.dp)
                .align(Alignment.CenterHorizontally)
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
            Icon(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(28.dp),
                painter = painterResource(id = R.drawable.icon_play_name_recording),
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

@Composable
private fun NamesToGuessBlockComponent(names: List<FullBlessedNameEntity>, selectedName: MutableState<FullBlessedNameEntity?>) {
    Spacer(modifier = Modifier.height(64.dp))
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            HearOneNameOptionComponent(
                bNameEntity = names[0],
                isSelectedState = selectedName,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            HearOneNameOptionComponent(
                bNameEntity = names[1],
                isSelectedState = selectedName,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            HearOneNameOptionComponent(
                bNameEntity = names[2],
                isSelectedState = selectedName,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            HearOneNameOptionComponent(
                bNameEntity = names[3],
                isSelectedState = selectedName,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun HearOneNameOptionComponent(
    bNameEntity: FullBlessedNameEntity,
    isSelectedState: MutableState<FullBlessedNameEntity?>,
    modifier: Modifier = Modifier
) {
    val isSelected = remember { mutableStateOf(false) }

    LaunchedEffect(isSelectedState.value) {
        isSelected.value = isSelectedState.value == bNameEntity
    }

    Box(
        modifier = modifier
            .widthIn(min = 100.dp)
            .height(120.dp)
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
                isSelectedState.value = if (isSelectedState.value == bNameEntity) null else bNameEntity
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(20.dp),
            text = bNameEntity.arabicVersion,
            color = if (isSelected.value) {
                MaterialTheme.colorScheme.primary
            } else {
                Color.Gray
            },
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            lineHeight = 36.sp
        )
    }
}

@Composable
@Preview
private fun TrainingHearOneScreenPreview() {
    Allahs99NamesTheme {
        TrainingHearOneScreen()
    }
}
