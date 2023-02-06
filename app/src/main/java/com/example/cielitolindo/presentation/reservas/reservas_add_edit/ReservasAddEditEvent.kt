package com.example.cielitolindo.presentation.reservas.reservas_add_edit

import com.example.cielitolindo.domain.model.Casa
import com.example.cielitolindo.domain.model.Moneda
import java.time.LocalDate

sealed class ReservasAddEditEvent {
    data class EnteredCliente(val id: String, val nombre: String) : ReservasAddEditEvent()
    data class EnteredCasa(val value: Casa) : ReservasAddEditEvent()
    data class EnteredFechaIngreso(val date: LocalDate) : ReservasAddEditEvent()
    data class EnteredFechaEgreso(val date: LocalDate) : ReservasAddEditEvent()
    data class EnteredImporteTotal(val value: String) : ReservasAddEditEvent()
    data class EnteredMoneda(val value: Moneda) : ReservasAddEditEvent()
    data class EnteredObservaciones(val value: String) : ReservasAddEditEvent()
    object OnSave : ReservasAddEditEvent()
    object OnDiscard : ReservasAddEditEvent()
    object ShowClienteSearchDialog : ReservasAddEditEvent()
    object HideClienteSearchDialog : ReservasAddEditEvent()
}