package com.example.cielitolindo.presentation.pagos.pagos_main

import android.app.DatePickerDialog
import android.content.SharedPreferences
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cielitolindo.data.data_source.formatter
import com.example.cielitolindo.data.util.TypesConverter
import com.example.cielitolindo.domain.model.Casa
import com.example.cielitolindo.domain.model.Categoria
import com.example.cielitolindo.domain.model.Cobro
import com.example.cielitolindo.domain.model.Gasto
import com.example.cielitolindo.presentation.components.*
import com.example.cielitolindo.presentation.pagos.gastos_add_edit.GastosAddEditEvent
import com.example.cielitolindo.presentation.pagos.pagos_main.components.PagoInfoDetail
import com.example.cielitolindo.presentation.pagos.pagos_main.components.SectionHeader
import com.example.cielitolindo.presentation.pagos.pagos_main.util.DateDefinitionCriteria
import com.example.cielitolindo.presentation.pagos.pagos_main.util.DateGroupCriteria
import com.example.cielitolindo.presentation.pagos.pagos_main.util.PagoInfo
import com.example.cielitolindo.presentation.reservas.reservas_main.components.PeriodPicker
import com.example.cielitolindo.presentation.util.LoadingState
import com.example.cielitolindo.presentation.util.ScaffoldElementsState
import com.example.cielitolindo.ui.theme.tertiary
import java.time.LocalDate

@Composable
fun PagosMainScreen(
    onComposing: (ScaffoldElementsState) -> Unit,
    onShowSnackbar: (
        String, String?, SnackbarDuration
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
        onComposing(ScaffoldElementsState(topBar = {
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
        }, bottomBar = {
            BottomNav(currentRoute = BottomNavigationOptions.PAGOS,
                onNavigateToClientes = { onNavigateToClientes() },
                onNavigateToReservas = { onNavigateToReservas() },
                onNavigateToPagos = {})
        }))
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
        viewModel.onEvent(
            PagosEvent.SetCustomPeriod(
                Pair((sharedPreferences.getString(
                    "CUSTOM_PERIOD_START", state.customPeriod.first.format(
                        formatter
                    )
                ) ?: state.customPeriod.first.format(
                    formatter
                )).let { LocalDate.parse(it, formatter) }, (sharedPreferences.getString(
                    "CUSTOM_PERIOD_END", state.customPeriod.second.format(
                        formatter
                    )
                ) ?: state.customPeriod.second.format(
                    formatter
                )).let { LocalDate.parse(it, formatter) })
            )
        )
    }

    Refreshable(refreshFunction = viewModel::updatePagos) {
        PeriodPicker(
            currentPeriod = state.getPeriodString(),
            onPreviousPeriod = { viewModel.onEvent(PagosEvent.PreviousPeriod) },
            onNextPeriod = { viewModel.onEvent(PagosEvent.NextPeriod) },
            buttonsEnabled = state.loadingInfo.loadingState == LoadingState.READY,
            modifier = Modifier.padding(8.dp)
        )
        LoadingDependingContent(loadingInfo = state.loadingInfo) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                //Cobros
                SectionHeader(title = "Cobros", hideShowState = state.showCobrosDetail, onShow = {
                    viewModel.onEvent(PagosEvent.ShowCobrosDetail)
                }, onHide = {
                    viewModel.onEvent(PagosEvent.HideCobrosDetail)
                })
                if (state.showCobrosDetail) {
                    for (casa in state.cobros.keys) {
                        Text(
                            text = "Casa " + casa.stringName,
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.padding(top = 4.dp, start = 8.dp)
                        )
                        for (pagoInfo in state.cobros[casa]!!) {
                            if (pagoInfo.element is Cobro) PagoInfoDetail(
                                pagoInfo = pagoInfo,
                                onPostpendIconClick = { onNavigateToCobroDetail(pagoInfo.element.id) },
                                postpendIconColor = MaterialTheme.colors.onSurface,
                                importesColor = MaterialTheme.colors.tertiary,
                                dividerAtEnd = true
                            )
                        }
                    }
                    Divider(modifier = Modifier.fillMaxWidth())
                }
                for (pagoInfo in state.getCobrosSubtotals()) {
                    if (pagoInfo.element is Casa) PagoInfoDetail(
                        pagoInfo = pagoInfo,
                        prependIcon = Icons.Filled.Home,
                        prependIconColor = pagoInfo.element.getFirstColor(),
                        importesColor = MaterialTheme.colors.tertiary
                    )
                }
                Divider(modifier = Modifier.fillMaxWidth())
                PagoInfoDetail(
                    pagoInfo = state.getCobrosTotal(), importesColor = MaterialTheme.colors.tertiary
                )

                //Gastos
                SectionHeader(title = "Gastos", hideShowState = state.showGastosDetail, onShow = {
                    viewModel.onEvent(PagosEvent.ShowGastosDetail)
                }, onHide = {
                    viewModel.onEvent(PagosEvent.HideGastosDetail)
                })
                if (state.showGastosDetail) {
                    for (categoria in state.gastos.keys) {
                        Text(
                            text = categoria.stringName,
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.padding(top = 4.dp, start = 8.dp)
                        )
                        for (pagoInfo in state.gastos[categoria]!!) {
                            if (pagoInfo.element is Gasto) PagoInfoDetail(
                                pagoInfo = pagoInfo,
                                onPostpendIconClick = { onNavigateToGastoDetail(pagoInfo.element.id) },
                                postpendIconColor = MaterialTheme.colors.onSurface,
                                importesColor = MaterialTheme.colors.error,
                                dividerAtEnd = true
                            )
                        }
                    }
                    Divider(modifier = Modifier.fillMaxWidth())
                }
                PagoInfoDetail(
                    pagoInfo = PagoInfo(
                        descripcion = "AGREGAR GASTO",
                        importes = mapOf(),
                    ),
                    onPostpendIconClick = { onNavigateToCreateGasto() },
                    postpendIconColor = MaterialTheme.colors.error,
                    importesColor = MaterialTheme.colors.error,
                    postpendIcon = Icons.Filled.AddCircle,
                    dividerAtEnd = true,
                    noImportes = true
                )
                for (pagoInfo in state.getGastosSubtotals()) {
                    if (pagoInfo.element is Categoria) PagoInfoDetail(
                        pagoInfo = pagoInfo,
                        prependIcon = pagoInfo.element.getIcon(),
                        prependIconColor = MaterialTheme.colors.error,
                        importesColor = MaterialTheme.colors.error
                    )
                }
                Divider(modifier = Modifier.fillMaxWidth())
                PagoInfoDetail(
                    pagoInfo = state.getGastosTotal(), importesColor = MaterialTheme.colors.error
                )

                //Ganancias
                SectionHeader(title = "Ganancias", hideShowState = null, onShow = {}, onHide = {})
                PagoInfoDetail(
                    pagoInfo = state.getGananciasTotal(),
                    importesColor = MaterialTheme.colors.tertiary
                )
                Divider(modifier = Modifier.fillMaxWidth())
            }
        }
    }

    val context = LocalContext.current

    val startDatePicker = DatePickerDialog(
        context,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            val selectedDate = LocalDate.of(mYear, mMonth + 1, mDayOfMonth)
            val edit = sharedPreferences.edit()
            edit.putString("CUSTOM_PERIOD_START", selectedDate.format(formatter))
            if(selectedDate.isAfter(state.customPeriod.second)) {
                edit.putString("CUSTOM_PERIOD_END", selectedDate.plusMonths(3).format(formatter))
                viewModel.onEvent(
                    PagosEvent.SetCustomPeriod(
                        Pair(selectedDate, selectedDate.plusMonths(3))
                    )
                )
            }
            else {
                viewModel.onEvent(
                    PagosEvent.SetCustomPeriod(
                        state.customPeriod.copy(first = selectedDate)
                    )
                )
            }
            edit.apply()
        },
        state.customPeriod.first.year,
        state.customPeriod.first.monthValue - 1,
        state.customPeriod.first.dayOfMonth
    )

    val endDatePicker = DatePickerDialog(
        context,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            val selectedDate = LocalDate.of(mYear, mMonth + 1, mDayOfMonth)
            if(selectedDate.isAfter(state.customPeriod.first)) {
                val edit = sharedPreferences.edit()
                edit.putString("CUSTOM_PERIOD_END", selectedDate.format(formatter))
                edit.apply()
                viewModel.onEvent(
                    PagosEvent.SetCustomPeriod(
                        state.customPeriod.copy(second = selectedDate)
                    )
                )
            } else {
                onShowSnackbar("La fecha de final debe ser posterior a la inicial!", null, SnackbarDuration.Short)
            }
        },
        state.customPeriod.second.year,
        state.customPeriod.second.monthValue - 1,
        state.customPeriod.second.dayOfMonth
    )

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
                            text = "Visualización de transacciones",
                            style = MaterialTheme.typography.h6
                        )
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        )
                        Text(
                            text = "Períodos",
                            style = MaterialTheme.typography.body1,
                            fontWeight = FontWeight.Medium
                        )
                        Row {
                            DefaultRadioButton(text = "Por Mes",
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
                            DefaultRadioButton(text = "Por Temporada",
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
                        DefaultRadioButton(text = "Personalizado",
                            selected = state.dateGroupCriteria == DateGroupCriteria.CUSTOM,
                            onClick = {
                                val edit = sharedPreferences.edit()
                                edit.putString("DATE_GROUP_CRITERIA", "CUSTOM")
                                edit.apply()
                                viewModel.onEvent(
                                    PagosEvent.SetDateGroupCriteria(
                                        DateGroupCriteria.CUSTOM
                                    )
                                )
                            })
                        if(state.dateGroupCriteria == DateGroupCriteria.CUSTOM) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RoundedCornerIconButton(
                                    onClick = { startDatePicker.show() },
                                    icon = Icons.Filled.EditCalendar,
                                    contentDescription = "Seleccionar Fecha Inicial",
                                    buttonSize = 36.dp,
                                    iconSize = 28.dp
                                )
                                Text(state.getPeriodString(), modifier = Modifier.padding(horizontal = 8.dp))
                                RoundedCornerIconButton(
                                    onClick = { endDatePicker.show() },
                                    icon = Icons.Filled.EditCalendar,
                                    contentDescription = "Seleccionar Fecha Final",
                                    buttonSize = 36.dp,
                                    iconSize = 28.dp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Fecha de cobro definida por",
                            style = MaterialTheme.typography.body1,
                            fontWeight = FontWeight.Medium
                        )
                        Row {
                            DefaultRadioButton(text = "Fecha de pago",
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
                            DefaultRadioButton(text = "Fecha de reserva",
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
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}