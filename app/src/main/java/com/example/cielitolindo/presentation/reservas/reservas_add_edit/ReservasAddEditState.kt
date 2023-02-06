package com.example.cielitolindo.presentation.reservas.reservas_add_edit

import com.example.cielitolindo.domain.model.Casa
import com.example.cielitolindo.domain.model.Cliente
import com.example.cielitolindo.domain.model.Moneda
import com.example.cielitolindo.presentation.util.LoadingInfo
import com.example.cielitolindo.presentation.util.TextFieldState
import java.time.LocalDate
import java.util.*

data class ReservasAddEditState(
    val screenTitle: String = "Nueva Reserva",
    val showClienteSearchDialog: Boolean = false,
    val allClientes: List<Cliente> = listOf(),
    val loadingInfo: LoadingInfo = LoadingInfo(),
    val id: String = UUID.randomUUID().toString(),
    val cliente: TextFieldState = TextFieldState(label = "Cliente"),
    val clienteId: String = "",
    val casa: Casa? = null,
    val fechaIngreso: LocalDate? = null,
    val fechaEgreso: LocalDate? = null,
    val importeTotal: TextFieldState = TextFieldState(label = "Importe Total"),
    val moneda: Moneda? = null,
    val observaciones: TextFieldState = TextFieldState(label = "Observaciones"),
) {
    val isSaveEnabled: Boolean
        get() = cliente.text.isNotBlank() &&
                casa != null &&
                fechaIngreso != null &&
                fechaEgreso != null &&
                importeTotal.text.isNotBlank() &&
                moneda != null &&
                fechaEgreso.isAfter(fechaIngreso)
}