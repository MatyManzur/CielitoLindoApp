package com.example.cielitolindo.presentation.reservas.reservas_detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cielitolindo.domain.model.getDireccionCompleta
import com.example.cielitolindo.domain.model.getNombreCompleto
import com.example.cielitolindo.domain.model.getRangoDeFechasString
import com.example.cielitolindo.presentation.clientes.clientes_detail.ClientesDetailEvent
import com.example.cielitolindo.presentation.clientes.clientes_detail.ClientesDetailVM
import com.example.cielitolindo.presentation.clientes.clientes_detail.components.ReservaButton
import com.example.cielitolindo.presentation.components.NameValueText
import com.example.cielitolindo.presentation.components.RoundedCornerIconButton
import com.example.cielitolindo.presentation.util.LoadingState
import com.example.cielitolindo.presentation.util.ScaffoldElementsState
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (state.reserva != null) {
            Text(text = "Datos de la Reserva", style = MaterialTheme.typography.h5, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                NameValueText(fieldName = "Nombre del Cliente", fieldValue = state.clienteName, modifier = Modifier.weight(1f))
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
            NameValueText(fieldName = "Fechas de Ingreso y Egreso", fieldValue = state.reserva.getRangoDeFechasString("dd MMM yyyy"))
            Spacer(modifier = Modifier.height(8.dp))
            NameValueText(fieldName = "Importe Total", fieldValue = state.reserva.moneda.importeToString(state.reserva.importeTotal))
            Spacer(modifier = Modifier.height(8.dp))
            if(!state.reserva.observaciones.isNullOrBlank()) {
                NameValueText(fieldName = "Observaciones", fieldValue = state.reserva.observaciones)
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