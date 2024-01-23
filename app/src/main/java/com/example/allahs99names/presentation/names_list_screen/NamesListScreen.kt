package com.example.allahs99names.presentation.names_list_screen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
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
import com.example.allahs99names.core.Empty
import com.example.allahs99names.domain.model.FullBlessedNameEntity
import com.example.allahs99names.ui.theme.Allahs99NamesTheme
import com.example.allahs99names.ui.utils.rippleClickable

@Composable
fun NamesListScreen() {
    val viewModel: NamesListViewModel = hiltViewModel()
    val state = viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        SearchItem(viewModel)
        if (state.value.namesList.isEmpty()) {
            EmptySearch()
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(state.value.namesList) {
                    BlessedNameItem(it) { isLearned, arabicName ->
                        viewModel.changeSavedNameLearnedState(isLearned, arabicName)
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchItem(viewModel: NamesListViewModel) {
    val searchQuery = viewModel.searchQuery
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 24.dp, end = 16.dp, bottom = 16.dp),
        value = searchQuery.value,
        onValueChange = { input ->
            searchQuery.value = input
            viewModel.onSearchQueryChanged(input)
        },
        placeholder = {
            Text(
                text = stringResource(id = R.string.names_list_search_hint)
            )
        },
        trailingIcon = if (searchQuery.value.isNotEmpty()) {
            {
                IconButton(
                    onClick = {
                        searchQuery.value = String.Empty
                        viewModel.onSearchQueryChanged(String.Empty)
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.clear_search_icon),
                        contentDescription = null
                    )
                }
            }
        } else {
            {}
        }
    )
}

@Composable
private fun EmptySearch() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = stringResource(id = R.string.names_list_empty_search_message),
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 26.sp
        )
    }
}

@Composable
private fun BlessedNameItem(
    bNameEntity: FullBlessedNameEntity,
    changeSavedLearnedNameState: (Boolean, String) -> Unit
) {
    val isOpened = remember { mutableStateOf(false) }
    val isLearned = remember { mutableStateOf(bNameEntity.isLearned) }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 8.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(12.dp),
            )
            .rippleClickable {
                isOpened.value = !isOpened.value
            }
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(start = 12.dp, top = 8.dp)
                    .align(Alignment.TopStart),
                text = "${bNameEntity.nameCount}.",
                color = Color.White
            )
            Icon(
                tint = Color.White,
                modifier = Modifier
                    .padding(top = 8.dp, end = 12.dp)
                    .rotate(
                        degrees = animateFloatAsState(
                            label = "Rotation",
                            animationSpec = tween(durationMillis = HEIGHT_CHANGE_MILLIS_VALUE),
                            targetValue = if (isOpened.value) {
                                DOWN_ARROW_ROTATION_OPENED_VALUE
                            } else {
                                DOWN_ARROW_ROTATION_INITIAL_VALUE
                            }
                        ).value
                    )
                    .align(Alignment.TopEnd),
                painter = painterResource(id = R.drawable.name_item_down_arrow),
                contentDescription = null
            )
        }
        Text(
            text = bNameEntity.arabicVersion,
            fontSize = 38.sp,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(start = 12.dp, end = 12.dp),
            textAlign = TextAlign.Center
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(13.dp)
                .padding(top = 6.dp, end = 84.dp, start = 84.dp, bottom = 6.dp)
                .background(Color.White)
        )

        Text(
            text = bNameEntity.transliteration,
            fontSize = 24.sp,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(start = 12.dp, end = 12.dp),
            textAlign = TextAlign.Center
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(13.dp)
                .padding(top = 6.dp, end = 84.dp, start = 84.dp, bottom = 6.dp)
                .background(Color.White)
        )
        Text(
            text = bNameEntity.russianVersion,
            fontSize = 24.sp,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(start = 12.dp, end = 12.dp, bottom = 8.dp),
            textAlign = TextAlign.Center
        )
        AnimatedVisibility(
            visible = isOpened.value,
            modifier = Modifier
                .fillMaxWidth(),
            enter = expandVertically(
                animationSpec = tween(
                    durationMillis = HEIGHT_CHANGE_MILLIS_VALUE,
                    easing = EaseInOut
                )
            ),
            exit = shrinkVertically(
                animationSpec = tween(
                    durationMillis = HEIGHT_CHANGE_MILLIS_VALUE,
                    easing = EaseInOut
                )
            ),
            content = {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        IconButton(
                            onClick = {
                                isLearned.value = !isLearned.value
                                changeSavedLearnedNameState.invoke(
                                    isLearned.value,
                                    bNameEntity.arabicVersion
                                )
                            }
                        ) {
                            Icon(
                                tint = Color.White,
                                painter = if (isLearned.value) {
                                    painterResource(id = R.drawable.name_added_learned_icon)
                                } else {
                                    painterResource(
                                        id = R.drawable.add_learned_name_icon
                                    )
                                },
                                contentDescription = null
                            )
                        }
                        IconButton(
                            onClick = {
                                Toast
                                    .makeText(context, "Воспроизводим запись!", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        ) {
                            Icon(
                                tint = Color.White,
                                painter = painterResource(id = R.drawable.icon_play_name_recording),
                                contentDescription = null
                            )
                        }
                    }
                    Text(
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                        text = bNameEntity.russianMeaning,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                }
            }
        )
    }
}

private const val HEIGHT_CHANGE_MILLIS_VALUE = 300
private const val DOWN_ARROW_ROTATION_INITIAL_VALUE = 0f
private const val DOWN_ARROW_ROTATION_OPENED_VALUE = 180f

@Composable
@Preview
private fun NamesListScreenPreview() {
    Allahs99NamesTheme {
        NamesListScreen()
    }
}