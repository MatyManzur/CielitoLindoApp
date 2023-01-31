package com.example.cielitolindo.presentation.clientes.clientes_main

import com.example.cielitolindo.domain.model.Cliente
import com.example.cielitolindo.domain.util.ClienteOrder

sealed class ClientesEvent {
    data class Order(val clienteOrder: ClienteOrder) : ClientesEvent()
    object ToggleOrderSection : ClientesEvent()
    object FetchClientes : ClientesEvent()
}
