package com.example.cielitolindo.presentation.reservas.reservas_main

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cielitolindo.domain.model.Casa
import com.example.cielitolindo.presentation.components.BottomNav
import com.example.cielitolindo.presentation.components.BottomNavigationOptions
import com.example.cielitolindo.presentation.reservas.reservas_main.components.*
import com.example.cielitolindo.presentation.util.LoadingInfo
import com.example.cielitolindo.presentation.util.LoadingState
import com.example.cielitolindo.presentation.util.ScaffoldElementsState

@Composable
fun ReservasMainScreen(
    onComposing: (ScaffoldElementsState) -> Unit,
    onShowSnackbar: (
        String,
        String?,
        SnackbarDuration
    ) -> Unit,
    onNavigateToCreateReserva: () -> Unit,
    onNavigateToReservaDetail: (String) -> Unit,
    onNavigateToClientes: () -> Unit,
    onNavigateToPagos: () -> Unit,
    viewModel: ReservasMainVM = hiltViewModel()
) {
    val state = viewModel.state.value
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true, key2 = state.activeCasa) {
        onComposing(
            ScaffoldElementsState(
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { onNavigateToCreateReserva() },
                        backgroundColor = state.activeCasa?.getSecondColor() ?: MaterialTheme.colors.secondary
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Add", tint = state.activeCasa?.getOnColor() ?: MaterialTheme.colors.onSecondary)
                    }
                },
                topBar = {
                    TopAppBar(
                        title = {
                            Text(text = "Reservas" + if(state.activeCasa != null) " - Casa ${state.activeCasa}" else "", color = state.activeCasa?.getOnColor() ?: MaterialTheme.colors.onPrimary)
                        },
                        actions = {
                            IconButton(onClick = { viewModel.onEvent(ReservasEvent.ToggleCasa) }) {
                                Icon(
                                    imageVector = Icons.Filled.HolidayVillage,
                                    contentDescription = "Cambiar Casa",
                                    tint = state.activeCasa?.getOnColor() ?: MaterialTheme.colors.onPrimary
                                )
                            }
                        },
                        backgroundColor = state.activeCasa?.getFirstColor() ?: MaterialTheme.colors.primary,
                    )
                },
                bottomBar = {
                    BottomNav(
                        currentRoute = BottomNavigationOptions.RESERVAS,
                        onNavigateToClientes = { onNavigateToClientes() },
                        onNavigateToReservas = {},
                        onNavigateToPagos = { onNavigateToPagos() },
                        backgroundColor = state.activeCasa?.getFirstColor() ?: MaterialTheme.colors.primary,
                        onColor = state.activeCasa?.getOnColor() ?: MaterialTheme.colors.onPrimary
                    )
                }
            )
        )
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        MonthPicker(
            currentMonth = state.yearMonth,
            onPreviousMonth = { viewModel.onEvent(ReservasEvent.PreviousMonth) },
            onNextMonth = { viewModel.onEvent(ReservasEvent.NextMonth) },
            modifier = Modifier.padding(8.dp),
            buttonsEnabled = state.loadingInfo.loadingState == LoadingState.READY
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
        ) {
            val horizontalPadding: Dp = 2.dp
            Column {
                WeekdaysLabels(modifier = Modifier.padding(horizontal = horizontalPadding))
                Divider()
                for ((i, week) in state.weeks.withIndex()) {
                    DaysNumberLabels(
                        startingDay = week.first(),
                        modifier = Modifier.padding(horizontal = horizontalPadding),
                        month = state.yearMonth.month,
                        todayColor = state.activeCasa?.getSecondColor() ?: MaterialTheme.colors.secondary
                    )
                    when (state.activeCasa) {
                        null -> {
                            for (c in Casa.values()) {
                                val reservasWeekOfCasa = state.reservasWeeks[i].filter { r -> r.reserva.casa == c }
                                val onColor = c.getOnColor()
                                ReservasStripe(
                                    onClickReserva = { onNavigateToReservaDetail(it.id) },
                                    getClienteNameAtIndex = { reservasWeekOfCasa[it].clienteName },
                                    onColor = onColor,
                                    reservas = reservasWeekOfCasa.map { ri -> ri.reserva },
                                    firstDayOfWeek = week.first(),
                                    stripeSize = StripeSize.SINGLE_LINE
                                )
                            }
                        }
                        else -> {
                            val reservasWeek = state.reservasWeeks[i]
                            val onColor = state.activeCasa.getOnColor()
                            ReservasStripe(
                                onClickReserva = { onNavigateToReservaDetail(it.id) },
                                getClienteNameAtIndex = { reservasWeek[it].clienteName },
                                onColor = onColor,
                                reservas = reservasWeek.map { ri -> ri.reserva },
                                firstDayOfWeek = week.first(),
                                stripeSize = StripeSize.EXPANDED
                            )
                        }
                    }
                    Divider()
                }
            }
            //Líneas verticales
            Row(
                modifier = Modifier.padding(horizontal = horizontalPadding),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Divider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                )
                for (i in 1..7) {
                    Spacer(modifier = Modifier.weight(1f))
                    Divider(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(1.dp)
                    )
                }
            }
        }
    }
}