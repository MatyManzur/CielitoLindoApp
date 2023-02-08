package com.example.cielitolindo.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Refreshable(
    refreshFunction: suspend () -> Unit,
    content: @Composable () -> Unit,
) {
    var refreshing by remember { mutableStateOf(false) }
    val refreshScope = rememberCoroutineScope()
    fun refresh() = refreshScope.launch {
        refreshing = true
        refreshFunction()
        refreshing = false
    }
    val pullRefreshState = rememberPullRefreshState(
        refreshing,
        ::refresh
    )
    Box(Modifier.pullRefresh(pullRefreshState)) {
        Column(modifier = Modifier.wrapContentSize()) {
            content()
        }
        PullRefreshIndicator(
            refreshing = refreshing, state = pullRefreshState, Modifier.align(
                Alignment.TopCenter
            )
        )
    }
}