package com.example.cielitolindo.presentation.reservas.reservas_detail

import com.example.cielitolindo.domain.model.Cobro
import com.example.cielitolindo.domain.model.Reserva
import com.example.cielitolindo.presentation.util.LoadingInfo

data class ReservasDetailState(
    val id: String = "",
    val clienteName: String = "",
    val reserva: Reserva? = null,
    val cobros: List<Cobro> = listOf(),
    val showDeleteConfirmationDialog: Boolean = false,
    val loadingInfo: LoadingInfo = LoadingInfo()
) {
    fun getSaldoPendiente(): Float {
        var ans = reserva?.importeTotal ?: 0f
        for (cobro in cobros) {
            ans -= if (cobro.moneda == reserva?.moneda) cobro.importe else cobro.enConceptoDe ?: 0f
        }
        return ans
    }
}