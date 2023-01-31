package com.example.cielitolindo.presentation.clientes.clientes_main

import com.example.cielitolindo.domain.model.Cliente
import com.example.cielitolindo.domain.util.ClienteOrder
import com.example.cielitolindo.domain.util.OrderType

data class ClientesState(
    val isLoading: Boolean = true,
    val clientes: List<Cliente> = emptyList(),
    val clientesOrder: ClienteOrder = ClienteOrder.FechaInscripcion(
        OrderType.Descending
    ),
    val isOrderSectionVisible: Boolean = false
) {
    val isEmpty: Boolean
        get() = clientes.isEmpty()
}
