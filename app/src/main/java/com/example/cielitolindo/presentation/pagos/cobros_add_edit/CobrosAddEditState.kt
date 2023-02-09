package com.example.cielitolindo.presentation.pagos.cobros_add_edit

import com.example.cielitolindo.domain.model.Cliente
import com.example.cielitolindo.domain.model.Moneda
import com.example.cielitolindo.domain.model.Reserva
import com.example.cielitolindo.presentation.util.LoadingInfo
import com.example.cielitolindo.presentation.util.TextFieldState
import java.time.LocalDate
import java.util.*

data class CobrosAddEditState(
    val screenTitle: String = "Nuevo cobro",
    val loadingInfo: LoadingInfo = LoadingInfo(),
    val id: String = UUID.randomUUID().toString(),
    val cliente: Cliente? = null,
    val reserva: Reserva? = null,
    val fechaPago: LocalDate = LocalDate.now(),
    val importe: TextFieldState = TextFieldState(label = "Importe"),
    val moneda: Moneda? = reserva?.moneda,
    val enConceptoDe: TextFieldState = TextFieldState(label = "En concepto de " + (reserva?.moneda?.unitString ?: "")),
    val modoPago: TextFieldState = TextFieldState(label = "Modo de Pago"),
    val descripcion: TextFieldState = TextFieldState(label = "Descripcion"),
) {
    val isSaveEnabled: Boolean
        get() = cliente != null &&
                reserva != null &&
                importe.text.isNotBlank() &&
                moneda != null &&
                (reserva.moneda == moneda || enConceptoDe.text.isNotBlank())
}
