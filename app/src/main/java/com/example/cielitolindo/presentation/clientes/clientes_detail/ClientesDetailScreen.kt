package com.example.cielitolindo.presentation.clientes.clientes_detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cielitolindo.domain.model.getDireccionCompleta
import com.example.cielitolindo.domain.model.getNombreCompleto
import com.example.cielitolindo.presentation.components.NameValueText
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
                        backgroundColor = MaterialTheme.colors.primary
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Editar Cliente"
                        )
                    }
                },
                topBar = {
                    TopAppBar(
                        title = {
                            Text(text = "Detalle de Cliente")
                        },
                        navigationIcon = {
                            IconButton(onClick = { onNavigateUp() }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "Atras"
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = { viewModel.onEvent(ClientesDetailEvent.OnShowDeleteConfirmationDialog) }) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Eliminar Cliente"
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (state.cliente != null) {
            NameValueText(fieldName = "Nombre", fieldValue = state.cliente.getNombreCompleto())
            Spacer(modifier = Modifier.height(8.dp))
            NameValueText(fieldName = "DNI", fieldValue = state.cliente.dni.toString())
            Spacer(modifier = Modifier.height(8.dp))
            NameValueText(
                fieldName = "Dirección",
                fieldValue = state.cliente.getDireccionCompleta()
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (state.cliente.telefono != null) {
                NameValueText(fieldName = "Teléfono", fieldValue = state.cliente.telefono)
                Spacer(modifier = Modifier.height(8.dp))
            }
            if (state.cliente.email != null) {
                NameValueText(fieldName = "Email", fieldValue = state.cliente.email)
                Spacer(modifier = Modifier.height(8.dp))
            }
            if (state.cliente.observaciones != null) {
                NameValueText(fieldName = "Observaciones", fieldValue = state.cliente.observaciones)
                Spacer(modifier = Modifier.height(8.dp))
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
                    if(state.loadingInfo.loadingState == LoadingState.READY) {
                        Text(text = "Sí")
                    }
                    else {
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