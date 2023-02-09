package com.example.cielitolindo.presentation.pagos.gastos_add_edit

import com.example.cielitolindo.domain.model.Categoria
import com.example.cielitolindo.domain.model.Moneda
import java.time.LocalDate

sealed class GastosAddEditEvent{
    data class EnteredFecha(val date: LocalDate): GastosAddEditEvent()
    data class EnteredDescripcion(val value: String): GastosAddEditEvent()
    data class EnteredImporte(val value: String): GastosAddEditEvent()
    data class EnteredMoneda(val moneda: Moneda): GastosAddEditEvent()
    data class EnteredCategoria(val categoria: Categoria): GastosAddEditEvent()
    data class ExpandDropdownMenu(val value: Boolean? = null): GastosAddEditEvent()
    object OnSave : GastosAddEditEvent()
    object OnDiscard : GastosAddEditEvent()
}
