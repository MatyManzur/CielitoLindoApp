package com.example.cielitolindo.presentation.pagos.gastos_add_edit

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.cielitolindo.domain.model.Categoria
import com.example.cielitolindo.domain.model.Moneda
import com.example.cielitolindo.presentation.components.DefaultRadioButton
import com.example.cielitolindo.presentation.components.RoundedCornerIconButton
import com.example.cielitolindo.presentation.util.LoadingState
import com.example.cielitolindo.presentation.util.ScaffoldElementsState
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GastosAddEditScreen(
    onComposing: (ScaffoldElementsState) -> Unit,
    onShowSnackbar: (
        String,
        String?,
        SnackbarDuration
    ) -> Unit,
    onNavigateUp: () -> Unit,
    viewModel: GastosAddEditVM = hiltViewModel(),
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
                                viewModel.onEvent(GastosAddEditEvent.OnSave)
                        },
                        backgroundColor = if (state.isSaveEnabled) MaterialTheme.colors.secondary else Color.Gray
                    ) {
                        if (state.loadingInfo.loadingState == LoadingState.READY) {
                            Icon(
                                imageVector = Icons.Filled.Save,
                                contentDescription = "Guardar Gasto",
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
                                    viewModel.onEvent(GastosAddEditEvent.OnDiscard)
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
                GastosAddEditVM.UiEvent.DiscardGasto -> onNavigateUp()
                GastosAddEditVM.UiEvent.SaveGasto -> onNavigateUp()
                is GastosAddEditVM.UiEvent.ShowSnackbar -> onShowSnackbar(
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
                GastosAddEditEvent.EnteredFecha(
                    LocalDate.of(
                        mYear,
                        mMonth + 1,
                        mDayOfMonth
                    )
                )
            )
        }, state.fecha.year, state.fecha.monthValue - 1, state.fecha.dayOfMonth
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
    ) {
        ExposedDropdownMenuBox(
            expanded = state.dropdownMenuExpanded,
            onExpandedChange = {
                viewModel.onEvent(GastosAddEditEvent.ExpandDropdownMenu())
            }
        ) {
            OutlinedTextField(
                value = state.categoria?.stringName ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text(text = "Categoría") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = state.dropdownMenuExpanded
                    )
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                leadingIcon = {
                    Icon(imageVector = state.categoria?.getIcon() ?: Icons.Filled.Category, contentDescription = state.categoria?.stringName ?: "Categoría")
                }
            )

            // menu
            ExposedDropdownMenu(
                expanded = state.dropdownMenuExpanded,
                onDismissRequest = { viewModel.onEvent(GastosAddEditEvent.ExpandDropdownMenu(false)) }
            ) {
                Categoria.values().forEach { categoria ->
                    DropdownMenuItem(onClick = {
                        viewModel.onEvent(GastosAddEditEvent.EnteredCategoria(categoria))
                        viewModel.onEvent(GastosAddEditEvent.ExpandDropdownMenu(false))
                    }) {
                        Icon(categoria.getIcon(), categoria.stringName)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = categoria.stringName)
                    }
                }
            }
        }
        OutlinedTextField(
            value = state.descripcion.text,
            onValueChange = {
                viewModel.onEvent(GastosAddEditEvent.EnteredDescripcion(it))
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
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
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
                Text(text = "Fecha del Gasto", style = MaterialTheme.typography.body1)
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = state.fecha.format(formatter),
                        style = MaterialTheme.typography.h6
                    )
                    RoundedCornerIconButton(
                        onClick = { datePicker.show() },
                        icon = Icons.Filled.EditCalendar,
                        contentDescription = "Seleccionar Fecha del Gasto"
                    )
                }
            }
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp)
        )
        Text(text = "Moneda del gasto", style = MaterialTheme.typography.body1)
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            for (m in Moneda.values()) {
                DefaultRadioButton(
                    text = m.stringName,
                    selected = state.moneda == m,
                    onClick = { viewModel.onEvent(GastosAddEditEvent.EnteredMoneda(m)) },
                )
            }
        }
        OutlinedTextField(
            value = state.importe.text,
            onValueChange = {
                viewModel.onEvent(GastosAddEditEvent.EnteredImporte(it))
            },
            label = {
                Text(text = state.importe.label + " en " + state.moneda.unitString)
            },
            textStyle = MaterialTheme.typography.body1,
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                keyboardType = KeyboardType.Number,
            ),
            singleLine = true,
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
    }

}