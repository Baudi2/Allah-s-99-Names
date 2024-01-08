package com.example.allahs99names.presentation.names_list_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.allahs99names.ui.theme.Allahs99NamesTheme

@Composable
fun NamesListScreen() {
    val viewModel: NamesListViewModel = hiltViewModel()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = "This is the Names list screen"
        )
    }
}

@Composable
@Preview
private fun NamesListScreenPreview() {
    Allahs99NamesTheme {
        NamesListScreen()
    }
}