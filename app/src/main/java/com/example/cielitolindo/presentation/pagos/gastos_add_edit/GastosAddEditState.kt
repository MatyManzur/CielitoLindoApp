package com.example.cielitolindo.presentation.pagos.gastos_add_edit

import com.example.cielitolindo.domain.model.Categoria
import com.example.cielitolindo.domain.model.Moneda
import com.example.cielitolindo.presentation.util.LoadingInfo
import com.example.cielitolindo.presentation.util.TextFieldState
import java.time.LocalDate
import java.util.*

data class GastosAddEditState(
    val screenTitle: String = "Nuevo gasto",
    val loadingInfo: LoadingInfo = LoadingInfo(),
    val id: String = UUID.randomUUID().toString(),
    val fecha: LocalDate = LocalDate.now(),
    val importe: TextFieldState = TextFieldState(label = "Importe"),
    val moneda: Moneda = Moneda.PESOS,
    val categoria: Categoria? = null,
    val descripcion: TextFieldState = TextFieldState(label = "Descripcion"),
    val dropdownMenuExpanded: Boolean = false
) {
    val isSaveEnabled: Boolean
        get() = categoria != null &&
                importe.text.isNotBlank()
}
