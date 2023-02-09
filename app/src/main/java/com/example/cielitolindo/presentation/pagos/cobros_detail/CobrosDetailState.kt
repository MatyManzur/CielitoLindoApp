package com.example.cielitolindo.presentation.pagos.cobros_detail

import com.example.cielitolindo.domain.model.Cliente
import com.example.cielitolindo.domain.model.Cobro
import com.example.cielitolindo.domain.model.Reserva
import com.example.cielitolindo.presentation.util.LoadingInfo

data class CobrosDetailState(
    val id: String = "",
    val cobro: Cobro? = null,
    val cliente: Cliente? = null,
    val reserva: Reserva? = null,
    val showDeleteConfirmationDialog: Boolean = false,
    val loadingInfo: LoadingInfo = LoadingInfo()
)
