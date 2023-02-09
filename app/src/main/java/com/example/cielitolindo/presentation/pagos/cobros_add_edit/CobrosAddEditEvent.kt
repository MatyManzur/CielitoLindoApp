package com.example.cielitolindo.presentation.pagos.cobros_add_edit

import com.example.cielitolindo.domain.model.Moneda
import java.time.LocalDate

sealed class CobrosAddEditEvent {
    data class EnteredFechaPago(val date: LocalDate): CobrosAddEditEvent()
    data class EnteredModoPago(val value: String): CobrosAddEditEvent()
    data class EnteredDescripcion(val value: String): CobrosAddEditEvent()
    data class EnteredImporte(val value: String): CobrosAddEditEvent()
    data class EnteredEnConceptoDe(val value: String): CobrosAddEditEvent()
    data class EnteredMoneda(val moneda: Moneda): CobrosAddEditEvent()
    object OnSave : CobrosAddEditEvent()
    object OnDiscard : CobrosAddEditEvent()
}
