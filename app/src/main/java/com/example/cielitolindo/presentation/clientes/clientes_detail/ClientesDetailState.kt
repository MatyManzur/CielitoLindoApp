package com.example.cielitolindo.presentation.clientes.clientes_detail

import com.example.cielitolindo.domain.model.Cliente
import com.example.cielitolindo.domain.model.Reserva
import com.example.cielitolindo.presentation.util.LoadingInfo
import com.example.cielitolindo.presentation.util.LoadingState

data class ClientesDetailState(
    val id: String = "",
    val cliente: Cliente? = null,
    val reservasOfCliente: List<Reserva> = listOf(),
    val showDeleteConfirmationDialog: Boolean = false,
    val loadingInfo: LoadingInfo = LoadingInfo()
)