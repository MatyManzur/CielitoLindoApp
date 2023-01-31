package com.example.cielitolindo.presentation.clientes.clientes_main.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cielitolindo.domain.util.ClienteOrder
import com.example.cielitolindo.domain.util.OrderType
import com.example.cielitolindo.presentation.components.DefaultRadioButton

@Composable
fun ClienteOrderSection(
    modifier: Modifier = Modifier,
    clienteOrder: ClienteOrder = ClienteOrder.FechaInscripcion(orderType = OrderType.Descending),
    onOrderChange: (ClienteOrder) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                text = "Fecha de Inscripción",
                selected = clienteOrder is ClienteOrder.FechaInscripcion,
                onClick = {
                    onOrderChange(ClienteOrder.FechaInscripcion(clienteOrder.orderType))
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Nombre",
                selected = clienteOrder is ClienteOrder.Nombre,
                onClick = {
                    onOrderChange(ClienteOrder.Nombre(clienteOrder.orderType))
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Apellido",
                selected = clienteOrder is ClienteOrder.Apellido,
                onClick = {
                    onOrderChange(ClienteOrder.Apellido(clienteOrder.orderType))
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                text = "Ascendente",
                selected = clienteOrder.orderType is OrderType.Ascending,
                onClick = {
                    onOrderChange(clienteOrder.copy(OrderType.Ascending))
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Descendente",
                selected = clienteOrder.orderType is OrderType.Descending,
                onClick = {
                    onOrderChange(clienteOrder.copy(OrderType.Descending))
                }
            )
        }
    }
}