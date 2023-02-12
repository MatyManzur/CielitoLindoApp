package com.example.cielitolindo.presentation.reservas.reservas_detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cielitolindo.domain.model.Moneda
import com.example.cielitolindo.domain.model.getRangoDeFechasString
import com.example.cielitolindo.presentation.components.NameValueText
import com.example.cielitolindo.presentation.components.Refreshable
import com.example.cielitolindo.presentation.components.RoundedCornerIconButton
import com.example.cielitolindo.presentation.pagos.pagos_main.components.PagoInfoDetail
import com.example.cielitolindo.presentation.pagos.pagos_main.util.PagoInfo
import com.example.cielitolindo.presentation.util.LoadingState
import com.example.cielitolindo.presentation.util.ScaffoldElementsState
import com.example.cielitolindo.ui.theme.tertiary
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ReservasDetailScreen(
    onComposing: (ScaffoldElementsState) -> Unit,
    onShowSnackbar: (
        message: String,
        actionLabel: String?,
        duration: SnackbarDuration
    ) -> Unit,
    onNavigateUp: () -> Unit,
    onNavigateToEditReserva: (String) -> Unit,
    onNavigateToClienteDetail: (String) -> Unit,
    onNavigateToCreateCobro: (String) -> Unit,
    onNavigateToCobroDetail: (String) -> Unit,
    viewModel: ReservasDetailVM = hiltViewModel()
) {
    val state = viewModel.state.value

    LaunchedEffect(key1 = true) {
        onComposing(
            ScaffoldElementsState(
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            viewModel.onEvent(ReservasDetailEvent.OnEdit)
                        },
                        backgroundColor = MaterialTheme.colors.secondary
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Editar Reserva",
                            tint = MaterialTheme.colors.onSecondary
                        )
                    }
                },
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = "Detalle de Reserva",
                                color = MaterialTheme.colors.onPrimary
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { onNavigateUp() }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "Atrás",
                                    tint = MaterialTheme.colors.onPrimary
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = { viewModel.onEvent(ReservasDetailEvent.OnShowDeleteConfirmationDialog) }) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Eliminar Reserva",
                                    tint = MaterialTheme.colors.onPrimary
                                )
                            }
                        },
                        backgroundColor = MaterialTheme.colors.primary,
                    )
                }
            )
        )
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is ReservasDetailVM.UiEvent.EditReserva -> {
                    onNavigateToEditReserva(event.reservaId)
                }
                ReservasDetailVM.UiEvent.Exit -> {
                    onNavigateUp()
                }
                is ReservasDetailVM.UiEvent.ShowSnackbar -> {
                    onShowSnackbar(event.message, null, SnackbarDuration.Short)
                }
            }
        }
    }

    Refreshable(refreshFunction = viewModel::updateReserva) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            if (state.reserva != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Datos de la Reserva",
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        NameValueText(
                            fieldName = "Nombre del Cliente",
                            fieldValue = state.clienteName,
                            modifier = Modifier.weight(1f)
                        )
                        RoundedCornerIconButton(
                            onClick = { onNavigateToClienteDetail(state.reserva.clienteId) },
                            icon = Icons.Filled.PersonSearch,
                            contentDescription = "Ver cliente",
                            buttonSize = 36.dp,
                            iconSize = 28.dp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    NameValueText(fieldName = "Casa", fieldValue = state.reserva.casa.stringName)
                    Spacer(modifier = Modifier.height(8.dp))
                    NameValueText(
                        fieldName = "Fechas de Ingreso y Egreso",
                        fieldValue = state.reserva.getRangoDeFechasString("dd MMM yyyy")
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (!state.reserva.observaciones.isNullOrBlank()) {
                        NameValueText(
                            fieldName = "Observaciones",
                            fieldValue = state.reserva.observaciones
                        )
                    }
                }
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                )
                PagoInfoDetail(
                    pagoInfo = PagoInfo(
                        descripcion = "Importe Total",
                        importes = mapOf(Pair(state.reserva.moneda, state.reserva.importeTotal))
                    ),
                    importesColor = MaterialTheme.colors.onSurface,
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                )
                for (cobro in state.cobros) {
                    val map = mutableMapOf<Moneda, Float>()
                    map[cobro.moneda] = cobro.importe
                    if (cobro.moneda != state.reserva.moneda)
                        map[state.reserva.moneda] = cobro.enConceptoDe ?: 0f
                    PagoInfoDetail(
                        pagoInfo = PagoInfo(
                            descripcion = if (cobro.modoPago.isNullOrBlank()) "Cobro" else cobro.modoPago,
                            importes = map,
                            fecha = cobro.fechaPago,
                        ),
                        importesColor = MaterialTheme.colors.onSurface,
                        onPostpendIconClick = { onNavigateToCobroDetail(cobro.id) },
                        postpendIconColor = MaterialTheme.colors.onSurface,
                        connectingImportes = if (state.reserva.moneda == Moneda.PESOS) "<" else ">"
                    )
                }
                PagoInfoDetail(
                    pagoInfo = PagoInfo(
                        descripcion = "AGREGAR COBRO",
                        importes = mapOf(),
                    ),
                    onPostpendIconClick = { onNavigateToCreateCobro(state.id) },
                    postpendIconColor = MaterialTheme.colors.tertiary,
                    importesColor = MaterialTheme.colors.tertiary,
                    postpendIcon = Icons.Filled.AddCircle,
                    dividerAtEnd = true,
                    noImportes = true
                )
                val saldoPendiente = state.getSaldoPendiente()
                PagoInfoDetail(
                    pagoInfo = PagoInfo(
                        descripcion = "Saldo Pendiente",
                        importes = mapOf(Pair(state.reserva.moneda, saldoPendiente))
                    ),
                    importesColor = if (saldoPendiente > 0.01f) MaterialTheme.colors.error else MaterialTheme.colors.tertiary,
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

    if (state.showDeleteConfirmationDialog) {
        AlertDialog(
            onDismissRequest = {
                viewModel.onEvent(ReservasDetailEvent.OnHideDeleteConfirmationDialog)
            },
            title = {
                Text(text = "¿Está seguro que desea eliminar la reserva?")
            },
            text = {
                Text(text = "Esta acción no se puede deshacer.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.onEvent(ReservasDetailEvent.OnDelete)
                    }
                ) {
                    if (state.loadingInfo.loadingState == LoadingState.READY) {
                        Text(text = "Sí")
                    } else {
                        CircularProgressIndicator(
                            color = MaterialTheme.colors.onSecondary,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 3.dp
                        )
                    }
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        viewModel.onEvent(ReservasDetailEvent.OnHideDeleteConfirmationDialog)
                    }
                ) {
                    Text(text = "No")
                }
            },
        )
    }
}