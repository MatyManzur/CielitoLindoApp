package com.example.cielitolindo.presentation.pagos.gastos_detail

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
import com.example.cielitolindo.presentation.components.NameValueText
import com.example.cielitolindo.presentation.components.Refreshable
import com.example.cielitolindo.presentation.util.LoadingState
import com.example.cielitolindo.presentation.util.ScaffoldElementsState
import kotlinx.coroutines.flow.collectLatest
import java.time.format.DateTimeFormatter

@Composable
fun GastosDetailScreen(
    onComposing: (ScaffoldElementsState) -> Unit,
    onShowSnackbar: (
        message: String,
        actionLabel: String?,
        duration: SnackbarDuration
    ) -> Unit,
    onNavigateUp: () -> Unit,
    onNavigateToEditGasto: (String) -> Unit,
    viewModel: GastosDetailVM = hiltViewModel()
) {
    val state = viewModel.state.value
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    LaunchedEffect(key1 = true) {
        onComposing(
            ScaffoldElementsState(
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            viewModel.onEvent(GastosDetailEvent.OnEdit)
                        },
                        backgroundColor = MaterialTheme.colors.secondary
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Editar Gasto",
                            tint = MaterialTheme.colors.onSecondary
                        )
                    }
                },
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = "Detalle de Gasto",
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
                            IconButton(onClick = { viewModel.onEvent(GastosDetailEvent.OnShowDeleteConfirmationDialog) }) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Eliminar Gasto",
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
                is GastosDetailVM.UiEvent.EditGasto -> {
                    onNavigateToEditGasto(event.gastoId)
                }
                GastosDetailVM.UiEvent.Exit -> {
                    onNavigateUp()
                }
                is GastosDetailVM.UiEvent.ShowSnackbar -> {
                    onShowSnackbar(event.message, null, SnackbarDuration.Short)
                }
            }
        }
    }

    Refreshable(refreshFunction = viewModel::updateGasto) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            if (state.gasto != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Datos del gasto",
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    NameValueText(
                        fieldName = "Categoría",
                        fieldValue = state.gasto.categoria.stringName
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (!state.gasto.descripcion.isNullOrBlank()) {
                        NameValueText(
                            fieldName = "Descripción",
                            fieldValue = state.gasto.descripcion
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    NameValueText(
                        fieldName = "Fecha de Gasto",
                        fieldValue = state.gasto.fecha.format(formatter)
                    )
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    )
                    NameValueText(
                        fieldName = "Importe",
                        fieldValue = state.gasto.moneda.importeToString(state.gasto.importe)
                    )
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    }

    if (state.showDeleteConfirmationDialog) {
        AlertDialog(
            onDismissRequest = {
                viewModel.onEvent(GastosDetailEvent.OnHideDeleteConfirmationDialog)
            },
            title = {
                Text(text = "¿Está seguro que desea eliminar el gasto?")
            },
            text = {
                Text(text = "Esta acción no se puede deshacer.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.onEvent(GastosDetailEvent.OnDelete)
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
                        viewModel.onEvent(GastosDetailEvent.OnHideDeleteConfirmationDialog)
                    }
                ) {
                    Text(text = "No")
                }
            },
        )
    }
}