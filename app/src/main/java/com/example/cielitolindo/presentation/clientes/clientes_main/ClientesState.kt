package com.example.cielitolindo.presentation.clientes.clientes_main

import com.example.cielitolindo.domain.model.Cliente
import com.example.cielitolindo.domain.model.Moneda
import com.example.cielitolindo.domain.model.Reserva
import com.example.cielitolindo.domain.util.ClienteOrder
import com.example.cielitolindo.domain.util.OrderType
import com.example.cielitolindo.presentation.util.LoadingInfo
import com.example.cielitolindo.presentation.util.LoadingState

data class ClientesState(
    val loadingInfo: LoadingInfo = LoadingInfo(LoadingState.LOADING),
    val clientes: List<Cliente> = emptyList(),
    val reservasOfClientes: MutableMap<String, List<Reserva>> = mutableMapOf(),
    val saldosPendientesOfClientes: MutableMap<String, Map<Moneda, Float>> = mutableMapOf(),
    val clientesOrder: ClienteOrder = ClienteOrder.FechaInscripcion(
        OrderType.Descending
    ),
    val isOrderSectionVisible: Boolean = false
) {
    val isEmpty: Boolean
        get() = clientes.isEmpty()
}
