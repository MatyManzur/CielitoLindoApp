package com.example.cielitolindo.presentation.pagos.pagos_main

import com.example.cielitolindo.domain.model.Casa
import com.example.cielitolindo.domain.model.Categoria
import com.example.cielitolindo.domain.model.Moneda
import com.example.cielitolindo.presentation.pagos.pagos_main.util.DateDefinitionCriteria
import com.example.cielitolindo.presentation.pagos.pagos_main.util.DateGroupCriteria
import com.example.cielitolindo.presentation.pagos.pagos_main.util.PagoInfo
import com.example.cielitolindo.presentation.pagos.pagos_main.util.Temporada
import com.example.cielitolindo.presentation.util.LoadingInfo
import com.example.cielitolindo.presentation.util.LoadingState
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth
import java.time.format.DateTimeFormatter

data class PagosState(
    val yearMonth: YearMonth = YearMonth.now(),
    val temporada: Temporada = Temporada(YearMonth.now()),
    val customPeriod: Pair<LocalDate, LocalDate> = Pair(LocalDate.now().minusMonths(3), LocalDate.now()),
    val cobros: Map<Casa, List<PagoInfo>> = mapOf(),
    val cobrosSubtotals: List<PagoInfo> = listOf(),
    val cobrosTotal: PagoInfo = PagoInfo(descripcion = "", importes = mapOf()),
    val gastos: Map<Categoria, List<PagoInfo>> = mapOf(),
    val gastosSubtotals: List<PagoInfo> = listOf(),
    val gastosTotal: PagoInfo = PagoInfo(descripcion = "", importes = mapOf()),
    val dateGroupCriteria: DateGroupCriteria = DateGroupCriteria.BY_MONTH,
    val dateDefinitionCriteria: DateDefinitionCriteria = DateDefinitionCriteria.BY_PAYMENT_DATE,
    val cobrosLoadingInfo: LoadingInfo = LoadingInfo(LoadingState.LOADING),
    val gastosLoadingInfo: LoadingInfo = LoadingInfo(LoadingState.LOADING),
    val showCobrosDetail: Boolean = false,
    val showGastosDetail: Boolean = false,
    val showSettingsDialog: Boolean = false
) {
    val loadingInfo: LoadingInfo
    get() {
        if (cobrosLoadingInfo.loadingState == LoadingState.ERROR || cobrosLoadingInfo.loadingState == LoadingState.ERROR) {
            return LoadingInfo(LoadingState.ERROR, "Ocurrió un error!")
        }
        if (cobrosLoadingInfo.loadingState == LoadingState.LOADING || cobrosLoadingInfo.loadingState == LoadingState.LOADING) {
            return LoadingInfo(LoadingState.LOADING)
        }
        if (cobrosLoadingInfo.loadingState == LoadingState.SUCCESS || cobrosLoadingInfo.loadingState == LoadingState.SUCCESS) {
            return LoadingInfo(LoadingState.SUCCESS)
        }
        return LoadingInfo(LoadingState.READY)
    }

    fun getDateInterval(): Pair<LocalDate, LocalDate> {
        return when (dateGroupCriteria) {
            DateGroupCriteria.BY_MONTH -> Pair(yearMonth.atDay(1), yearMonth.atEndOfMonth())
            DateGroupCriteria.BY_TEMPORADA -> Pair(temporada.firstDay, temporada.lastDay)
            DateGroupCriteria.CUSTOM -> customPeriod
        }
    }

    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yy")

    fun getPeriodString(): String {
        return when (dateGroupCriteria) {
            DateGroupCriteria.BY_MONTH -> yearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
            DateGroupCriteria.BY_TEMPORADA -> temporada.toString()
            DateGroupCriteria.CUSTOM -> "${customPeriod.first.format(formatter)} -> ${customPeriod.second.format(formatter)}"
        }
    }

    val gananciasTotal: PagoInfo
    get() {
        val importes = mutableMapOf<Moneda, Float>()
        val cobrosTotal = cobrosTotal.importes
        val gastosTotal = gastosTotal.importes
        for (moneda in Moneda.values()) {
            importes[moneda] = cobrosTotal[moneda] ?: 0f
            importes[moneda] = importes[moneda]?.minus(gastosTotal[moneda] ?: 0f) ?: 0f.minus(gastosTotal[moneda] ?: 0f)
        }
        return PagoInfo(descripcion = "Ganancias", importes = importes)
    }
}