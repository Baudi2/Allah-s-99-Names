package com.example.allahs99names.presentation.trainings.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.allahs99names.R
import com.example.allahs99names.ui.components.ButtonComponent
import com.example.allahs99names.ui.components.ButtonState
import com.example.allahs99names.ui.components.rememberModalNonClosableState
import com.example.allahs99names.ui.theme.Allahs99NamesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingSuccessfulModal(playSound: (Int) -> Unit, onContinueClicked: () -> Unit, onDismiss: () -> Unit = {}) {
    playSound.invoke(R.raw.correct)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background,
        sheetState = rememberModalNonClosableState(),
        content = {
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .size(36.dp),
                    painter = painterResource(id = R.drawable.ic_complete_check_mark),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = stringResource(id = R.string.training_successful_modal_title),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            ButtonComponent(
                modifier = Modifier
                    .padding(bottom = 44.dp),
                state = ButtonState.ENABLED,
                text = stringResource(id = R.string.training_successful_modal_button_text),
                onClick = onContinueClicked
            )
        },
        dragHandle = null
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingErrorModal(correctAnswer: String, playSound: (Int) -> Unit, onContinueClicked: () -> Unit, onDismiss: () -> Unit = {}) {
    playSound.invoke(R.raw.error)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background,
        sheetState = rememberModalNonClosableState(),
        content = {
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .size(28.dp),
                    painter = painterResource(id = R.drawable.clear_search_icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = stringResource(id = R.string.training_error_modal_title),
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 20.sp
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                modifier = Modifier.padding(horizontal = 20.dp),
                text = stringResource(id = R.string.training_error_modal_correct_answer_is),
                color = MaterialTheme.colorScheme.error,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                modifier = Modifier.padding(horizontal = 20.dp),
                text = correctAnswer,
                color = MaterialTheme.colorScheme.error,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            ButtonComponent(
                modifier = Modifier
                    .padding(bottom = 44.dp),
                state = ButtonState.ERROR,
                text = stringResource(id = R.string.training_error_modal_button_text),
                onClick = onContinueClicked
            )
        },
        dragHandle = null
    )
}

@Composable
@Preview
private fun TrainingSuccessfulModalPreview() {
    Allahs99NamesTheme {
        TrainingSuccessfulModal(
            playSound = {},
            onContinueClicked = {}
        )
    }
}

@Composable
@Preview
private fun TrainingErrorModalPreview() {
    Allahs99NamesTheme {
        TrainingErrorModal(
            correctAnswer = "Answer",
            playSound = {},
            onContinueClicked = {}
        )
    }
}
