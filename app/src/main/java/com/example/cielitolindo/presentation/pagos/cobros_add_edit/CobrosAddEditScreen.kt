package com.example.cielitolindo.presentation.pagos.cobros_add_edit

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cielitolindo.domain.model.Moneda
import com.example.cielitolindo.domain.model.getNombreCompleto
import com.example.cielitolindo.domain.model.getRangoDeFechasString
import com.example.cielitolindo.presentation.components.DefaultRadioButton
import com.example.cielitolindo.presentation.components.NameValueText
import com.example.cielitolindo.presentation.components.RoundedCornerIconButton
import com.example.cielitolindo.presentation.util.LoadingState
import com.example.cielitolindo.presentation.util.ScaffoldElementsState

import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate

@Composable
fun CobrosAddEditScreen(
    onComposing: (ScaffoldElementsState) -> Unit,
    onShowSnackbar: (
        String,
        String?,
        SnackbarDuration
    ) -> Unit,
    onNavigateUp: () -> Unit,
    viewModel: CobrosAddEditVM = hiltViewModel(),
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
                                viewModel.onEvent(CobrosAddEditEvent.OnSave)
                        },
                        backgroundColor = if (state.isSaveEnabled) MaterialTheme.colors.secondary else Color.Gray
                    ) {
                        if (state.loadingInfo.loadingState == LoadingState.READY) {
                            Icon(
                                imageVector = Icons.Filled.Save,
                                contentDescription = "Guardar Cobro",
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
                                    viewModel.onEvent(CobrosAddEditEvent.OnDiscard)
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
                CobrosAddEditVM.UiEvent.DiscardCobro -> onNavigateUp()
                CobrosAddEditVM.UiEvent.SaveCobro -> onNavigateUp()
                is CobrosAddEditVM.UiEvent.ShowSnackbar -> onShowSnackbar(
                    event.message,
                    null,
                    SnackbarDuration.Short
                )
            }
        }
    }

    val context = LocalContext.current

    val datePicker = DatePickerDialog(
        context,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            viewModel.onEvent(
                CobrosAddEditEvent.EnteredFechaPago(
                    LocalDate.of(
                        mYear,
                        mMonth + 1,
                        mDayOfMonth
                    )
                )
            )
        }, state.fechaPago.year, state.fechaPago.monthValue - 1, state.fechaPago.dayOfMonth
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
    ) {
        NameValueText(fieldName = "Cliente", fieldValue = state.cliente?.getNombreCompleto() ?: "")
        NameValueText(
            fieldName = "Reserva", fieldValue = "Casa ${state.reserva?.casa?.stringName ?: ""}: ${
                state.reserva?.getRangoDeFechasString("dd/MM/yy") ?: ""
            }"
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(text = "Fecha del Cobro", style = MaterialTheme.typography.body1)
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = state.fechaPago.format(formatter),
                        style = MaterialTheme.typography.h6
                    )
                    RoundedCornerIconButton(
                        onClick = { datePicker.show() },
                        icon = Icons.Filled.EditCalendar,
                        contentDescription = "Seleccionar Fecha de Cobro"
                    )
                }
            }
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp)
        )
        NameValueText(
            fieldName = "Importe Total de la Reserva",
            fieldValue = state.reserva?.moneda?.importeToString(state.reserva.importeTotal) ?: ""
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Moneda del cobro", style = MaterialTheme.typography.body1)
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            for (m in Moneda.values()) {
                DefaultRadioButton(
                    text = m.stringName,
                    selected = state.moneda == m,
                    onClick = { viewModel.onEvent(CobrosAddEditEvent.EnteredMoneda(m)) },
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
        ) {
            OutlinedTextField(
                value = state.importe.text,
                onValueChange = {
                    viewModel.onEvent(CobrosAddEditEvent.EnteredImporte(it))
                },
                label = {
                    Text(text = state.importe.label + if(state.moneda != null) " en " + state.moneda.unitString else "")
                },
                textStyle = MaterialTheme.typography.body1,
                keyboardOptions = KeyboardOptions(
                    autoCorrect = false,
                    keyboardType = KeyboardType.Number,
                ),
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            if(state.moneda != state.reserva?.moneda) {
                OutlinedTextField(
                    modifier = Modifier.padding(start = 16.dp).weight(1f),
                    value = state.enConceptoDe.text,
                    onValueChange = {
                        viewModel.onEvent(CobrosAddEditEvent.EnteredEnConceptoDe(it))
                    },
                    label = {
                        Text(text = state.enConceptoDe.label)
                    },
                    textStyle = MaterialTheme.typography.body1,
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                        keyboardType = KeyboardType.Number,
                    ),
                    singleLine = true,
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = state.modoPago.text,
            onValueChange = {
                viewModel.onEvent(CobrosAddEditEvent.EnteredModoPago(it))
            },
            label = {
                Text(text = state.modoPago.label)
            },
            textStyle = MaterialTheme.typography.body1,
            keyboardOptions = KeyboardOptions(
                autoCorrect = true,
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Sentences
            ),
            singleLine = true,
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
        OutlinedTextField(
            value = state.descripcion.text,
            onValueChange = {
                viewModel.onEvent(CobrosAddEditEvent.EnteredDescripcion(it))
            },
            label = {
                Text(text = state.descripcion.label)
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
}