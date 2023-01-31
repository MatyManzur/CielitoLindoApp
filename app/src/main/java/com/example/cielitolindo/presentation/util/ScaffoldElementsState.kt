package com.example.cielitolindo.presentation.util

import androidx.compose.runtime.Composable

data class ScaffoldElementsState (
    val topBar: @Composable () -> Unit = {},
    val bottomBar: @Composable () -> Unit = {},
    val floatingActionButton: @Composable () -> Unit = {},
)