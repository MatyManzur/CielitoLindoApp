package com.example.cielitolindo.presentation.reservas.reservas_add_edit.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.cielitolindo.data.data_source.formatter
import com.example.cielitolindo.domain.model.Cliente
import com.example.cielitolindo.domain.model.getDireccionCompleta
import com.example.cielitolindo.domain.model.getNombreCompleto

@Composable
fun ClienteSearchDialog(
    onDismiss: () -> Unit,
    onClienteSelected: (id: String, name: String) -> Unit,
    allClientes: List<Cliente>
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Surface(

        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                var search by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = search,
                    onValueChange = { search = it },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar"
                        )
                    },
                    label = { Text(text = "Buscar Cliente") },
                )
                Divider(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp))
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    val clientes = allClientes.sortedByDescending { c -> c.fechaInscripcion }
                    val cornerRadius = 4.dp
                    for (cliente in clientes.filter { c -> c.getNombreCompleto().contains(search, ignoreCase = true) }) {
                        Button(
                            onClick = {
                                onClienteSelected(cliente.id, cliente.getNombreCompleto())
                                onDismiss()
                            },
                            modifier = Modifier
                                .padding(8.dp),
                            shape = RoundedCornerShape(cornerRadius),
                            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface, contentColor = MaterialTheme.colors.onSurface),
                            border = BorderStroke(1.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.2f))
                        ) {
                            Column(
                                horizontalAlignment = Alignment.Start,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = cliente.getNombreCompleto(),
                                    style = MaterialTheme.typography.body1
                                )
                                Text(
                                    text = "Inscripción: ${cliente.fechaInscripcion.format(formatter)}",
                                    style = MaterialTheme.typography.body2
                                )
                                Text(
                                    text = "Dirección: ${cliente.getDireccionCompleta()}",
                                    style = MaterialTheme.typography.body2
                                )
                            }
                        }
                    }
                }

            }
        }

    }
}