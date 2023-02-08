package com.example.cielitolindo.presentation.pagos.pagos_main

import android.content.SharedPreferences
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cielitolindo.domain.model.Casa
import com.example.cielitolindo.domain.model.Categoria
import com.example.cielitolindo.domain.model.Cobro
import com.example.cielitolindo.domain.model.Gasto
import com.example.cielitolindo.presentation.components.BottomNav
import com.example.cielitolindo.presentation.components.BottomNavigationOptions
import com.example.cielitolindo.presentation.components.DefaultRadioButton
import com.example.cielitolindo.presentation.components.Refreshable
import com.example.cielitolindo.presentation.pagos.pagos_main.components.PagoInfoDetail
import com.example.cielitolindo.presentation.pagos.pagos_main.components.SectionHeader
import com.example.cielitolindo.presentation.pagos.pagos_main.util.DateDefinitionCriteria
import com.example.cielitolindo.presentation.pagos.pagos_main.util.DateGroupCriteria
import com.example.cielitolindo.presentation.reservas.reservas_main.components.PeriodPicker
import com.example.cielitolindo.presentation.util.LoadingState
import com.example.cielitolindo.presentation.util.ScaffoldElementsState
import com.example.cielitolindo.ui.theme.tertiaryVariant

@Composable
fun PagosMainScreen(
    onComposing: (ScaffoldElementsState) -> Unit,
    onShowSnackbar: (
        String,
        String?,
        SnackbarDuration
    ) -> Unit,
    onNavigateToCreateGasto: () -> Unit,
    onNavigateToCobroDetail: (String) -> Unit,
    onNavigateToGastoDetail: (String) -> Unit,
    onNavigateToClientes: () -> Unit,
    onNavigateToReservas: () -> Unit,
    sharedPreferences: SharedPreferences,
    viewModel: PagosMainVM = hiltViewModel()
) {
    val state = viewModel.state.value

    LaunchedEffect(key1 = true) {
        onComposing(
            ScaffoldElementsState(
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { onNavigateToCreateGasto() },
                        backgroundColor = MaterialTheme.colors.error
                    ) {
                        Icon(Icons.Filled.Add, "Nuevo Gasto")
                    }
                },
                topBar = {
                    TopAppBar(
                        title = {
                            Text(text = "Finanzas", color = MaterialTheme.colors.onPrimary)
                        },
                        actions = {
                            IconButton(onClick = { viewModel.onEvent(PagosEvent.ShowSettingsDialog) }) {
                                Icon(
                                    imageVector = Icons.Filled.Settings,
                                    contentDescription = "Opciones",
                                    tint = MaterialTheme.colors.onPrimary
                                )
                            }
                        },
                        backgroundColor = MaterialTheme.colors.primary,
                    )
                },
                bottomBar = {
                    BottomNav(
                        currentRoute = BottomNavigationOptions.PAGOS,
                        onNavigateToClientes = { onNavigateToClientes() },
                        onNavigateToReservas = { onNavigateToReservas() },
                        onNavigateToPagos = {}
                    )
                }
            )
        )
        viewModel.onEvent(
            PagosEvent.SetDateGroupCriteria(
                DateGroupCriteria.valueOf(
                    sharedPreferences.getString("DATE_GROUP_CRITERIA", "BY_MONTH") ?: "BY_MONTH"
                )
            )
        )
        viewModel.onEvent(
            PagosEvent.SetDateDefinitionCriteria(
                DateDefinitionCriteria.valueOf(
                    sharedPreferences.getString("DATE_DEFINITION_CRITERIA", "BY_PAYMENT_DATE")
                        ?: "BY_PAYMENT_DATE"
                )
            )
        )
    }

    Refreshable(refreshFunction = viewModel::updatePagos) {
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())) {
            PeriodPicker(
                currentPeriod = state.getPeriodString(),
                onPreviousPeriod = { viewModel.onEvent(PagosEvent.PreviousPeriod) },
                onNextPeriod = { viewModel.onEvent(PagosEvent.NextPeriod) },
                buttonsEnabled = state.loadingInfo.loadingState == LoadingState.READY,
                modifier = Modifier.padding(8.dp)
            )

            //Cobros
            SectionHeader(title = "Cobros", hideShowState = state.showCobrosDetail, onShow = {
                viewModel.onEvent(PagosEvent.ShowCobrosDetail)
            }, onHide = {
                viewModel.onEvent(PagosEvent.HideCobrosDetail)
            })
            if (state.showCobrosDetail) {
                for (casa in state.cobros.keys) {
                    Text(text = "Casa " + casa.stringName, style = MaterialTheme.typography.h6)
                    for (pagoInfo in state.cobros[casa]!!) {
                        if (pagoInfo.element is Cobro)
                            PagoInfoDetail(
                                pagoInfo = pagoInfo,
                                onSeeDetailClick = { onNavigateToCobroDetail(pagoInfo.element.id) },
                                onSeeDetailButtonColor = casa.getFirstColor(),
                                importesColor = MaterialTheme.colors.tertiaryVariant
                            )
                    }
                }
                Divider(modifier = Modifier.fillMaxWidth())
            }
            for (pagoInfo in state.getCobrosSubtotals()) {
                if (pagoInfo.element is Casa)
                    PagoInfoDetail(
                        pagoInfo = pagoInfo,
                        prependIcon = Icons.Filled.Home,
                        prependIconColor = pagoInfo.element.getFirstColor(),
                        importesColor = MaterialTheme.colors.tertiaryVariant
                    )
            }
            Divider(modifier = Modifier.fillMaxWidth())
            PagoInfoDetail(
                pagoInfo = state.getCobrosTotal(),
                importesColor = MaterialTheme.colors.tertiaryVariant
            )

            //Gastos
            SectionHeader(title = "Gastos", hideShowState = state.showGastosDetail, onShow = {
                viewModel.onEvent(PagosEvent.ShowGastosDetail)
            }, onHide = {
                viewModel.onEvent(PagosEvent.HideGastosDetail)
            })
            if (state.showGastosDetail) {
                for (categoria in state.gastos.keys) {
                    Text(text = categoria.stringName, style = MaterialTheme.typography.h6)
                    for (pagoInfo in state.gastos[categoria]!!) {
                        if (pagoInfo.element is Gasto)
                            PagoInfoDetail(
                                pagoInfo = pagoInfo,
                                onSeeDetailClick = { onNavigateToGastoDetail(pagoInfo.element.id) },
                                onSeeDetailButtonColor = MaterialTheme.colors.error,
                                importesColor = MaterialTheme.colors.error
                            )
                    }
                }
                Divider(modifier = Modifier.fillMaxWidth())
            }
            for (pagoInfo in state.getGastosSubtotals()) {
                if (pagoInfo.element is Categoria)
                    PagoInfoDetail(
                        pagoInfo = pagoInfo,
                        prependIcon = pagoInfo.element.getIcon(),
                        prependIconColor = MaterialTheme.colors.error,
                        importesColor = MaterialTheme.colors.error
                    )
            }
            Divider(modifier = Modifier.fillMaxWidth())
            PagoInfoDetail(
                pagoInfo = state.getGastosTotal(),
                importesColor = MaterialTheme.colors.error
            )

            //Ganancias
            SectionHeader(title = "Ganancias", hideShowState = null, onShow = {}, onHide = {})
            PagoInfoDetail(
                pagoInfo = state.getGananciasTotal(),
                importesColor = MaterialTheme.colors.tertiaryVariant
            )
            Divider(modifier = Modifier.fillMaxWidth())
        }
    }



    if (state.showSettingsDialog) {
        Dialog(
            onDismissRequest = { viewModel.onEvent(PagosEvent.HideSettingsDialog) },
        ) {
            Surface() {
                Column(horizontalAlignment = Alignment.Start) {
                    IconButton(onClick = { viewModel.onEvent(PagosEvent.HideSettingsDialog) }) {
                        Icon(Icons.Filled.Close, contentDescription = "Cerrar")
                    }
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "Visualización de pagos",
                            style = MaterialTheme.typography.h6
                        )
                        Divider(modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp))
                        Text(
                            text = "Tipo de Período",
                            style = MaterialTheme.typography.body1,
                            fontWeight = FontWeight.Medium
                        )
                        Row {
                            DefaultRadioButton(
                                text = "Por Mes",
                                selected = state.dateGroupCriteria == DateGroupCriteria.BY_MONTH,
                                onClick = {
                                    val edit = sharedPreferences.edit()
                                    edit.putString("DATE_GROUP_CRITERIA", "BY_MONTH")
                                    edit.apply()
                                    viewModel.onEvent(
                                        PagosEvent.SetDateGroupCriteria(
                                            DateGroupCriteria.BY_MONTH
                                        )
                                    )
                                })
                            DefaultRadioButton(
                                text = "Por Temporada",
                                selected = state.dateGroupCriteria == DateGroupCriteria.BY_TEMPORADA,
                                onClick = {
                                    val edit = sharedPreferences.edit()
                                    edit.putString("DATE_GROUP_CRITERIA", "BY_TEMPORADA")
                                    edit.apply()
                                    viewModel.onEvent(
                                        PagosEvent.SetDateGroupCriteria(
                                            DateGroupCriteria.BY_TEMPORADA
                                        )
                                    )
                                })
                        }
                        Text(
                            text = "Fecha de cobro definida por",
                            style = MaterialTheme.typography.body1,
                            fontWeight = FontWeight.Medium
                        )
                        Row {
                            DefaultRadioButton(
                                text = "Fecha de pago",
                                selected = state.dateDefinitionCriteria == DateDefinitionCriteria.BY_PAYMENT_DATE,
                                onClick = {
                                    val edit = sharedPreferences.edit()
                                    edit.putString("DATE_DEFINITION_CRITERIA", "BY_PAYMENT_DATE")
                                    edit.apply()
                                    viewModel.onEvent(
                                        PagosEvent.SetDateDefinitionCriteria(
                                            DateDefinitionCriteria.BY_PAYMENT_DATE
                                        )
                                    )
                                })
                            DefaultRadioButton(
                                text = "Fecha de reserva",
                                selected = state.dateDefinitionCriteria == DateDefinitionCriteria.BY_RESERVA_DATE,
                                onClick = {
                                    val edit = sharedPreferences.edit()
                                    edit.putString("DATE_DEFINITION_CRITERIA", "BY_RESERVA_DATE")
                                    edit.apply()
                                    viewModel.onEvent(
                                        PagosEvent.SetDateDefinitionCriteria(
                                            DateDefinitionCriteria.BY_RESERVA_DATE
                                        )
                                    )
                                })
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }


            }
        }
    }

}