package com.example.cielitolindo.presentation.pagos.pagos_main.util

import com.example.cielitolindo.domain.model.Element
import com.example.cielitolindo.domain.model.Moneda
import java.time.LocalDate

data class PagoInfo(val element: Element? = null, val descripcion: String, val fecha: LocalDate? = null, val importes: Map<Moneda, Float>)
