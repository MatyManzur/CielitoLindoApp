package com.example.cielitolindo.presentation.pagos.cobros_detail

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cielitolindo.domain.model.getNombreCompleto
import com.example.cielitolindo.domain.model.getRangoDeFechasString
import com.example.cielitolindo.presentation.components.NameValueText
import com.example.cielitolindo.presentation.components.Refreshable
import com.example.cielitolindo.presentation.components.RoundedCornerIconButton
import com.example.cielitolindo.presentation.util.LoadingState
import com.example.cielitolindo.presentation.util.ScaffoldElementsState
import kotlinx.coroutines.flow.collectLatest
import java.time.format.DateTimeFormatter

@Composable
fun CobrosDetailScreen(
    onComposing: (ScaffoldElementsState) -> Unit,
    onShowSnackbar: (
        message: String,
        actionLabel: String?,
        duration: SnackbarDuration
    ) -> Unit,
    onNavigateUp: () -> Unit,
    onNavigateToEditCobro: (String) -> Unit,
    onNavigateToReservaDetail: (String) -> Unit,
    onNavigateToClienteDetail: (String) -> Unit,
    viewModel: CobrosDetailVM = hiltViewModel()
) {
    val state = viewModel.state.value
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    LaunchedEffect(key1 = true) {
        onComposing(
            ScaffoldElementsState(
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            viewModel.onEvent(CobrosDetailEvent.OnEdit)
                        },
                        backgroundColor = MaterialTheme.colors.secondary
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Editar Cobro",
                            tint = MaterialTheme.colors.onSecondary
                        )
                    }
                },
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = "Detalle de Cobro",
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
                            IconButton(onClick = { viewModel.onEvent(CobrosDetailEvent.OnShowDeleteConfirmationDialog) }) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Eliminar Cobro",
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
                is CobrosDetailVM.UiEvent.EditCobro -> {
                    onNavigateToEditCobro(event.cobroId)
                }
                CobrosDetailVM.UiEvent.Exit -> {
                    onNavigateUp()
                }
                is CobrosDetailVM.UiEvent.ShowSnackbar -> {
                    onShowSnackbar(event.message, null, SnackbarDuration.Short)
                }
            }
        }
    }

    Refreshable(refreshFunction = viewModel::updateCobro) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            if (state.cobro != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Datos del cobro",
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        NameValueText(
                            fieldName = "Nombre del Cliente",
                            fieldValue = state.cliente?.getNombreCompleto() ?: "",
                            modifier = Modifier.weight(1f)
                        )
                        RoundedCornerIconButton(
                            onClick = { onNavigateToClienteDetail(state.cliente?.id ?: "") },
                            icon = Icons.Filled.PersonSearch,
                            contentDescription = "Ver cliente",
                            buttonSize = 36.dp,
                            iconSize = 28.dp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        NameValueText(
                            fieldName = "Reserva",
                            fieldValue = "Casa ${state.reserva?.casa?.stringName ?: ""}: ${state.reserva?.getRangoDeFechasString("dd/MM/yy")}",
                            modifier = Modifier.weight(1f)
                        )
                        RoundedCornerIconButton(
                            onClick = { onNavigateToReservaDetail(state.reserva?.id ?: "") },
                            icon = Icons.Filled.Event,
                            contentDescription = "Ver Reserva",
                            buttonSize = 36.dp,
                            iconSize = 28.dp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    NameValueText(
                        fieldName = "Fecha de Cobro",
                        fieldValue = state.cobro.fechaPago.format(formatter)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (!state.cobro.descripcion.isNullOrBlank()) {
                        NameValueText(
                            fieldName = "Descripcion",
                            fieldValue = state.cobro.descripcion
                        )
                    }
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    )
                    if (!state.cobro.modoPago.isNullOrBlank()) {
                        NameValueText(
                            fieldName = "Modo de Pago",
                            fieldValue = state.cobro.modoPago
                        )
                    }
                    NameValueText(
                        fieldName = "Importe",
                        fieldValue = state.cobro.moneda.importeToString(state.cobro.importe)
                    )
                    if(state.cobro.enConceptoDe != null && state.reserva != null) {
                        NameValueText(
                            fieldName = "En Concepto de",
                            fieldValue = state.reserva.moneda.importeToString(state.cobro.enConceptoDe)
                        )
                    }
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
                viewModel.onEvent(CobrosDetailEvent.OnHideDeleteConfirmationDialog)
            },
            title = {
                Text(text = "¿Está seguro que desea eliminar el cobro?")
            },
            text = {
                Text(text = "Esta acción no se puede deshacer.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.onEvent(CobrosDetailEvent.OnDelete)
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
                        viewModel.onEvent(CobrosDetailEvent.OnHideDeleteConfirmationDialog)
                    }
                ) {
                    Text(text = "No")
                }
            },
        )
    }
}