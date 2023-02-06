package com.example.cielitolindo.presentation.reservas.reservas_detail

import com.example.cielitolindo.domain.model.Reserva
import com.example.cielitolindo.presentation.util.LoadingInfo

data class ReservasDetailState(
    val id: String = "",
    val clienteName: String = "",
    val reserva: Reserva? = null,
    val showDeleteConfirmationDialog: Boolean = false,
    val loadingInfo: LoadingInfo = LoadingInfo()
)