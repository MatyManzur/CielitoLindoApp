package com.example.cielitolindo.presentation.clientes.clientes_detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cielitolindo.domain.model.getDireccionCompleta
import com.example.cielitolindo.domain.model.getNombreCompleto
import com.example.cielitolindo.presentation.clientes.clientes_detail.components.ReservaButton
import com.example.cielitolindo.presentation.components.NameValueText
import com.example.cielitolindo.presentation.components.Refreshable
import com.example.cielitolindo.presentation.util.LoadingState
import com.example.cielitolindo.presentation.util.ScaffoldElementsState
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ClientesDetailScreen(
    onComposing: (ScaffoldElementsState) -> Unit,
    onShowSnackbar: (
        message: String,
        actionLabel: String?,
        duration: SnackbarDuration
    ) -> Unit,
    onNavigateUp: () -> Unit,
    onNavigateToEditCliente: (String) -> Unit,
    onNavigateToReservaDetail: (String) -> Unit,
    viewModel: ClientesDetailVM = hiltViewModel()
) {
    val state = viewModel.state.value

    LaunchedEffect(key1 = true) {
        onComposing(
            ScaffoldElementsState(
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            viewModel.onEvent(ClientesDetailEvent.OnEdit)
                        },
                        backgroundColor = MaterialTheme.colors.secondary
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Editar Cliente",
                            tint = MaterialTheme.colors.onSecondary
                        )
                    }
                },
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = "Detalle de Cliente",
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
                            IconButton(onClick = { viewModel.onEvent(ClientesDetailEvent.OnShowDeleteConfirmationDialog) }) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Eliminar Cliente",
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
                is ClientesDetailVM.UiEvent.ShowSnackbar -> {
                    onShowSnackbar(event.message, null, SnackbarDuration.Short)
                }
                is ClientesDetailVM.UiEvent.EditCliente -> {
                    onNavigateToEditCliente(event.clienteId)
                }
                is ClientesDetailVM.UiEvent.Exit -> {
                    onNavigateUp()
                }
            }
        }
    }
    Refreshable(refreshFunction = viewModel::updateCliente) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (state.cliente != null) {
                Text(text = "Información del Cliente", style = MaterialTheme.typography.h5, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(8.dp))
                NameValueText(fieldName = "Nombre", fieldValue = state.cliente.getNombreCompleto())
                Spacer(modifier = Modifier.height(8.dp))
                if (state.cliente.dni != null) {
                    NameValueText(fieldName = "DNI", fieldValue = state.cliente.dni.toString())
                    Spacer(modifier = Modifier.height(8.dp))
                }
                if (!state.cliente.getDireccionCompleta().isBlank()) {
                    NameValueText(
                        fieldName = "Dirección",
                        fieldValue = state.cliente.getDireccionCompleta()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                if (!state.cliente.telefono.isNullOrBlank()) {
                    NameValueText(fieldName = "Teléfono", fieldValue = state.cliente.telefono)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                if (!state.cliente.email.isNullOrBlank()) {
                    NameValueText(fieldName = "Email", fieldValue = state.cliente.email)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                if (!state.cliente.observaciones.isNullOrBlank()) {
                    NameValueText(fieldName = "Observaciones", fieldValue = state.cliente.observaciones)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Divider(modifier = Modifier.fillMaxWidth())
                if (state.reservasOfCliente.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Reservas del Cliente", style = MaterialTheme.typography.h5, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        modifier = Modifier.padding(horizontal = 0.dp)
                    ) {
                        for (reserva in state.reservasOfCliente.sortedByDescending { r -> r.fechaEgreso }) {
                            ReservaButton(
                                reserva = reserva,
                                onClick = { onNavigateToReservaDetail(reserva.id) },
                                modifier = Modifier
                                    .padding(bottom = 8.dp)
                                    .height(32.dp),
                                textSize = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }

    if (state.showDeleteConfirmationDialog) {
        AlertDialog(
            onDismissRequest = {
                viewModel.onEvent(ClientesDetailEvent.OnHideDeleteConfirmationDialog)
            },
            title = {
                Text(text = "¿Está seguro que desea eliminar el cliente?")
            },
            text = {
                Text(text = "Esta acción no se puede deshacer.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.onEvent(ClientesDetailEvent.OnDelete)
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
                        viewModel.onEvent(ClientesDetailEvent.OnHideDeleteConfirmationDialog)
                    }
                ) {
                    Text(text = "No")
                }
            },
        )
    }

}