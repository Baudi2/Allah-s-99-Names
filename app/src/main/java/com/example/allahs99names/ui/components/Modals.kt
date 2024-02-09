package com.example.allahs99names.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberModalNonClosableState(): SheetState = rememberModalBottomSheetState(
    skipPartiallyExpanded = true,
    confirmValueChange = { false }
)
