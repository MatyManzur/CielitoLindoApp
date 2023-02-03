package com.example.cielitolindo.presentation.clientes.clientes_add_edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cielitolindo.presentation.clientes.clientes_detail.ClientesDetailVM
import com.example.cielitolindo.presentation.util.LoadingState
import com.example.cielitolindo.presentation.util.ScaffoldElementsState
import kotlinx.coroutines.flow.collectLatest


@Composable
fun ClientesAddEditScreen(
    onComposing: (ScaffoldElementsState) -> Unit,
    onShowSnackbar: (
        String,
        String?,
        SnackbarDuration
    ) -> Unit,
    onNavigateUp: () -> Unit,
    viewModel: ClientesAddEditVM = hiltViewModel(),
) {
    val state = viewModel.state.value

    LaunchedEffect(key1 = true, key2 = state.loadingInfo.loadingState == LoadingState.READY) {
        onComposing(
            ScaffoldElementsState(
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            viewModel.onEvent(ClientesAddEditEvent.OnSave)
                        },
                        backgroundColor = MaterialTheme.colors.secondary
                    ) {
                        if (state.loadingInfo.loadingState == LoadingState.READY) {
                            Icon(
                                imageVector = Icons.Filled.Save,
                                contentDescription = "Guardar Cliente",
                                tint = MaterialTheme.colors.onSecondary
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
                                    viewModel.onEvent(ClientesAddEditEvent.OnDiscard)
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
                is ClientesAddEditVM.UiEvent.ShowSnackbar -> {
                    onShowSnackbar(event.message, null, SnackbarDuration.Long)
                }
                is ClientesAddEditVM.UiEvent.SaveCliente -> {
                    onNavigateUp()
                }
                is ClientesAddEditVM.UiEvent.DiscardCliente -> {
                    onNavigateUp()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top
    ) {
        OutlinedTextField(
            value = state.nombre.text,
            onValueChange = {
                viewModel.onEvent(ClientesAddEditEvent.EnteredNombre(it))
            },
            label = {
                Text(text = state.nombre.label)
            },
            textStyle = MaterialTheme.typography.body1,
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words
            ),
            singleLine = true,
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = state.apellido.text,
            onValueChange = {
                viewModel.onEvent(ClientesAddEditEvent.EnteredApellido(it))
            },
            label = {
                Text(text = state.apellido.label)
            },
            textStyle = MaterialTheme.typography.body1,
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words
            ),
            singleLine = true,
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = state.dni.text,
            onValueChange = {
                viewModel.onEvent(ClientesAddEditEvent.EnteredDni(it))
            },
            label = {
                Text(text = state.dni.label)
            },
            textStyle = MaterialTheme.typography.body1,
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                keyboardType = KeyboardType.Number,
            ),
            singleLine = true,
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = state.direccion.text,
            onValueChange = {
                viewModel.onEvent(ClientesAddEditEvent.EnteredDireccion(it))
            },
            label = {
                Text(text = state.direccion.label)
            },
            textStyle = MaterialTheme.typography.body1,
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words
            ),
            singleLine = true,
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = state.localidad.text,
            onValueChange = {
                viewModel.onEvent(ClientesAddEditEvent.EnteredLocalidad(it))
            },
            label = {
                Text(text = state.localidad.label)
            },
            textStyle = MaterialTheme.typography.body1,
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words
            ),
            singleLine = true,
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = state.provincia.text,
            onValueChange = {
                viewModel.onEvent(ClientesAddEditEvent.EnteredProvincia(it))
            },
            label = {
                Text(text = state.provincia.label)
            },
            textStyle = MaterialTheme.typography.body1,
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words
            ),
            singleLine = true,
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = state.telefono.text,
            onValueChange = {
                viewModel.onEvent(ClientesAddEditEvent.EnteredTelefono(it))
            },
            label = {
                Text(text = state.telefono.label)
            },
            textStyle = MaterialTheme.typography.body1,
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                keyboardType = KeyboardType.Number,
            ),
            singleLine = true,
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = state.email.text,
            onValueChange = {
                viewModel.onEvent(ClientesAddEditEvent.EnteredEmail(it))
            },
            label = {
                Text(text = state.email.label)
            },
            textStyle = MaterialTheme.typography.body1,
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                keyboardType = KeyboardType.Email,
            ),
            singleLine = true,
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = state.observaciones.text,
            onValueChange = {
                viewModel.onEvent(ClientesAddEditEvent.EnteredObservaciones(it))
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

}