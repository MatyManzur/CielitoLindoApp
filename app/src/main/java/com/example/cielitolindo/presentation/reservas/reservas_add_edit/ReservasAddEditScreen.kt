package com.example.cielitolindo.presentation.reservas.reservas_add_edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentManager
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cielitolindo.R
import com.example.cielitolindo.domain.model.Casa
import com.example.cielitolindo.domain.model.Moneda
import com.example.cielitolindo.presentation.components.DefaultRadioButton
import com.example.cielitolindo.presentation.components.RoundedCornerIconButton
import com.example.cielitolindo.presentation.reservas.reservas_add_edit.components.ClienteSearchDialog
import com.example.cielitolindo.presentation.util.LoadingState
import com.example.cielitolindo.presentation.util.ScaffoldElementsState
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.flow.collectLatest
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

@Composable
fun ReservasAddEditScreen(
    onComposing: (ScaffoldElementsState) -> Unit,
    onShowSnackbar: (
        String,
        String?,
        SnackbarDuration
    ) -> Unit,
    onNavigateUp: () -> Unit,
    viewModel: ReservasAddEditVM = hiltViewModel(),
    supportFragmentManager: FragmentManager,
) {
    val state = viewModel.state.value
    val formatter = viewModel.formatter

    LaunchedEffect(
        key1 = true,
        key2 = state.loadingInfo.loadingState == LoadingState.READY,
        key3 = state.isSaveEnabled
    ) {
        onComposing(
            ScaffoldElementsState(
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            if (state.isSaveEnabled)
                                viewModel.onEvent(ReservasAddEditEvent.OnSave)
                        },
                        backgroundColor = if (state.isSaveEnabled) MaterialTheme.colors.secondary else Color.Gray
                    ) {
                        if (state.loadingInfo.loadingState == LoadingState.READY) {
                            Icon(
                                imageVector = Icons.Filled.Save,
                                contentDescription = "Guardar Reserva",
                                tint = MaterialTheme.colors.onSecondary.copy(alpha = if (state.isSaveEnabled) 1f else .5f)
                            )
                        } else {
                            CircularProgressIndicator(
                                color = MaterialTheme.colors.onSecondary,
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 3.dp
                            )
                        }
                    }
                },
                topBar = {
                    TopAppBar(
                        title = {
                            Text(text = state.screenTitle, color = MaterialTheme.colors.onPrimary)
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    viewModel.onEvent(ReservasAddEditEvent.OnDiscard)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Descartar Cambios",
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
                ReservasAddEditVM.UiEvent.DiscardReserva -> onNavigateUp()
                ReservasAddEditVM.UiEvent.SaveReserva -> onNavigateUp()
                is ReservasAddEditVM.UiEvent.ShowSnackbar -> onShowSnackbar(
                    event.message,
                    null,
                    SnackbarDuration.Short
                )
            }
        }
    }

    val picker = MaterialDatePicker.Builder.dateRangePicker()
        .setTitleText("Seleccione Fechas de Ingreso y Egreso")
        .build()

    picker.addOnPositiveButtonClickListener { selection ->
        val selectedFechaIngreso = Instant.ofEpochMilli(selection.first).atZone(ZoneId.of("UTC")).toLocalDate()
        viewModel.onEvent(ReservasAddEditEvent.EnteredFechaIngreso(selectedFechaIngreso))
        val selectedFechaEgreso = Instant.ofEpochMilli(selection.second).atZone(ZoneId.of("UTC")).toLocalDate()
        viewModel.onEvent(ReservasAddEditEvent.EnteredFechaEgreso(selectedFechaEgreso))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = state.cliente.text,
                onValueChange = {

                },
                label = {
                    Text(text = state.cliente.label)
                },
                textStyle = MaterialTheme.typography.body1,
                singleLine = true,
                enabled = false,
                modifier = Modifier
                    .padding(end = 12.dp)
                    .weight(1f)
            )
            RoundedCornerIconButton(
                onClick = { viewModel.onEvent(ReservasAddEditEvent.ShowClienteSearchDialog) },
                icon = Icons.Filled.PersonSearch,
                contentDescription = "Buscar Cliente"
            )
        }
        Divider(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp))
        Text(text = "Casa", style = MaterialTheme.typography.body1)
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            for (c in Casa.values()) {
                DefaultRadioButton(
                    text = c.stringName,
                    selected = state.casa == c,
                    onClick = { viewModel.onEvent(ReservasAddEditEvent.EnteredCasa(c)) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = c.getSecondColor(),
                    )
                )
            }
        }
        Divider(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
        ) {
            Column(modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(vertical = 8.dp)) {
                Text(text = "Fechas de Ingreso y Egreso", style = MaterialTheme.typography.body1)
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (state.fechaIngreso != null && state.fechaEgreso != null) "${
                            state.fechaIngreso.format(
                                formatter
                            )
                        } al ${state.fechaEgreso.format(formatter)}" else "--/--/-- al --/--/--",
                        style = MaterialTheme.typography.h6
                    )
                    RoundedCornerIconButton(
                        onClick = { picker.show(supportFragmentManager, "date_picker") },
                        icon = Icons.Filled.EditCalendar,
                        contentDescription = "Seleccionar Fecha Egreso"
                    )
                }
            }
        }
        Divider(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp))
        Text(text = "Moneda del importe", style = MaterialTheme.typography.body1)
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            for (m in Moneda.values()) {
                DefaultRadioButton(
                    text = m.stringName,
                    selected = state.moneda == m,
                    onClick = { viewModel.onEvent(ReservasAddEditEvent.EnteredMoneda(m)) },
                )
            }
        }
        OutlinedTextField(
            value = state.importeTotal.text,
            onValueChange = {
                viewModel.onEvent(ReservasAddEditEvent.EnteredImporteTotal(it))
            },
            label = {
                Text(text = state.importeTotal.label)
            },
            textStyle = MaterialTheme.typography.body1,
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                keyboardType = KeyboardType.Number,
            ),
            singleLine = true,
        )
        Divider(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp))
        OutlinedTextField(
            value = state.observaciones.text,
            onValueChange = {
                viewModel.onEvent(ReservasAddEditEvent.EnteredObservaciones(it))
            },
            label = {
                Text(text = state.observaciones.label)
            },
            textStyle = MaterialTheme.typography.body1,
            keyboardOptions = KeyboardOptions(
                autoCorrect = true,
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Sentences
            ),
            singleLine = false,
        )
    }

    if (state.showClienteSearchDialog) {
        ClienteSearchDialog(
            onDismiss = { viewModel.onEvent(ReservasAddEditEvent.HideClienteSearchDialog) },
            onClienteSelected = { id, name ->
                viewModel.onEvent(ReservasAddEditEvent.EnteredCliente(id, name))
            },
            allClientes = state.allClientes
        )
    }
}