package com.example.cielitolindo.presentation.pagos.pagos_main

import com.example.cielitolindo.domain.model.Casa
import com.example.cielitolindo.domain.model.Categoria
import com.example.cielitolindo.domain.model.Gasto
import com.example.cielitolindo.domain.model.Moneda
import com.example.cielitolindo.presentation.pagos.pagos_main.util.DateDefinitionCriteria
import com.example.cielitolindo.presentation.pagos.pagos_main.util.DateGroupCriteria
import com.example.cielitolindo.presentation.pagos.pagos_main.util.PagoInfo
import com.example.cielitolindo.presentation.pagos.pagos_main.util.Temporada
import com.example.cielitolindo.presentation.reservas.reservas_detail.ReservasDetailEvent
import com.example.cielitolindo.presentation.util.LoadingInfo
import com.example.cielitolindo.presentation.util.LoadingState
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth
import java.time.format.DateTimeFormatter

data class PagosState(
    val yearMonth: YearMonth = YearMonth.now(),
    val temporada: Temporada = Temporada(Year.now()),
    val cobros: Map<Casa, List<PagoInfo>> = mapOf(),
    val gastos: Map<Categoria, List<PagoInfo>> = mapOf(),
    val dateGroupCriteria: DateGroupCriteria = DateGroupCriteria.BY_MONTH,
    val dateDefinitionCriteria: DateDefinitionCriteria = DateDefinitionCriteria.BY_PAYMENT_DATE,
    val loadingInfo: LoadingInfo = LoadingInfo(LoadingState.LOADING),
    val showCobrosDetail: Boolean = false,
    val showGastosDetail: Boolean = false,
    val showSettingsDialog: Boolean = false
) {
    fun getDateInterval(): Pair<LocalDate, LocalDate> {
        return when (dateGroupCriteria) {
            DateGroupCriteria.BY_MONTH -> Pair(yearMonth.atDay(1), yearMonth.atEndOfMonth())
            DateGroupCriteria.BY_TEMPORADA -> Pair(temporada.firstDay, temporada.lastDay)
        }
    }

    fun getPeriodString(): String {
        return when (dateGroupCriteria) {
            DateGroupCriteria.BY_MONTH -> yearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
            DateGroupCriteria.BY_TEMPORADA -> temporada.toString()
        }
    }

    fun getCobrosSubtotals(): List<PagoInfo> {
        val ans = mutableListOf<PagoInfo>()
        for (casa in Casa.values()) {
            if (cobros.containsKey(casa) && cobros[casa] != null) {
                val importes = mutableMapOf<Moneda, Float>()
                for (pagoInfo in cobros[casa]!!) {
                    pagoInfo.importes.forEach { (moneda, importe) ->
                        importes[moneda] = importes[moneda]?.plus(importe) ?: 0f
                    }
                }
                ans.add(PagoInfo(casa,"Casa " + casa.stringName, importes = importes))
            }
        }
        return ans
    }

    fun getCobrosTotal(): PagoInfo {
        val importes = mutableMapOf<Moneda, Float>()
        for (pagoInfo in getCobrosSubtotals()) {
            pagoInfo.importes.forEach { (moneda, importe) ->
                importes[moneda] = importes[moneda]?.plus(importe) ?: 0f
            }
        }
        return PagoInfo(descripcion = "TOTAL", importes = importes)
    }

    fun getGastosSubtotals(): List<PagoInfo> {
        val ans = mutableListOf<PagoInfo>()
        for (categoria in gastos.keys) {
            if (gastos[categoria] != null) {
                val importes = mutableMapOf<Moneda, Float>()
                for (pagoInfo in gastos[categoria]!!) {
                    pagoInfo.importes.forEach { (moneda, importe) ->
                        importes[moneda] = importes[moneda]?.plus(importe) ?: 0f
                    }
                }
                ans.add(PagoInfo(categoria, categoria.stringName, importes = importes))
            }
        }
        return ans
    }

    fun getGastosTotal(): PagoInfo {
        val importes = mutableMapOf<Moneda, Float>()
        for (pagoInfo in getGastosSubtotals()) {
            pagoInfo.importes.forEach { (moneda, importe) ->
                importes[moneda] = importes[moneda]?.plus(importe) ?: 0f
            }
        }
        return PagoInfo(descripcion = "TOTAL", importes = importes)
    }

    fun getGananciasTotal(): PagoInfo {
        val importes = mutableMapOf<Moneda, Float>()
        val cobrosTotal = getCobrosTotal().importes
        val gastosTotal = getGastosTotal().importes
        for (moneda in Moneda.values()) {
            importes[moneda] = cobrosTotal[moneda] ?: 0f
            importes[moneda] = importes[moneda]?.minus(gastosTotal[moneda] ?: 0f) ?: 0f
        }
        return PagoInfo(descripcion = "Ganancias", importes = importes)
    }
}