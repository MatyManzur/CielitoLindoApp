package com.example.cielitolindo.presentation.pagos.pagos_main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cielitolindo.domain.model.*
import com.example.cielitolindo.domain.use_case.clientes.ClienteUseCases
import com.example.cielitolindo.domain.use_case.cobros.CobroUseCases
import com.example.cielitolindo.domain.use_case.gastos.GastoUseCases
import com.example.cielitolindo.domain.use_case.reservas.ReservaUseCases
import com.example.cielitolindo.presentation.pagos.pagos_main.util.DateDefinitionCriteria
import com.example.cielitolindo.presentation.pagos.pagos_main.util.DateGroupCriteria
import com.example.cielitolindo.presentation.pagos.pagos_main.util.PagoInfo
import com.example.cielitolindo.presentation.pagos.pagos_main.util.Temporada
import com.example.cielitolindo.presentation.util.LoadingInfo
import com.example.cielitolindo.presentation.util.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class PagosMainVM @Inject constructor(
    private val gastoUseCases: GastoUseCases,
    private val cobroUseCases: CobroUseCases,
    private val reservaUseCases: ReservaUseCases,
    private val clienteUseCases: ClienteUseCases,
) : ViewModel() {
    private val _state = mutableStateOf(PagosState())
    val state: State<PagosState> = _state

    private var getCobrosJob: Job? = null
    private var getGastosJob: Job? = null

    init {
        viewModelScope.launch {
            cobroUseCases.fetchCobros()
            gastoUseCases.fetchGastos()
        }
    }

    private fun setLoading() {
        _state.value = state.value.copy(
            cobrosLoadingInfo = LoadingInfo(LoadingState.LOADING),
            gastosLoadingInfo = LoadingInfo(LoadingState.LOADING)
        )
    }

    suspend fun updatePagos() {
        setLoading()
        cobroUseCases.fetchCobros()
        gastoUseCases.fetchGastos()
        getCobros()
        getGastos()
    }

    private fun calculateCobrosSubtotals(): List<PagoInfo> {
        val ans = mutableListOf<PagoInfo>()
        for (casa in Casa.values()) {
            if (state.value.cobros.containsKey(casa) && state.value.cobros[casa] != null) {
                val importes = mutableMapOf<Moneda, Float>()
                for (pagoInfo in state.value.cobros[casa]!!) {
                    pagoInfo.importes.forEach { (moneda, importe) ->
                        importes[moneda] = importes[moneda]?.plus(importe) ?: importe
                    }
                }
                ans.add(PagoInfo(casa, "C. " + casa.stringName, importes = importes))
            }
        }
        return ans
    }

    private fun calculateCobrosTotal(): PagoInfo {
        val importes = mutableMapOf<Moneda, Float>()
        for (pagoInfo in state.value.cobrosSubtotals) {
            pagoInfo.importes.forEach { (moneda, importe) ->
                importes[moneda] = importes[moneda]?.plus(importe) ?: importe
            }
        }
        return PagoInfo(descripcion = "TOTAL", importes = importes)
    }

    private fun calculateGastosSubtotals(): List<PagoInfo> {
        val ans = mutableListOf<PagoInfo>()
        for (categoria in state.value.gastos.keys) {
            if (state.value.gastos[categoria] != null) {
                val importes = mutableMapOf<Moneda, Float>()
                for (pagoInfo in state.value.gastos[categoria]!!) {
                    pagoInfo.importes.forEach { (moneda, importe) ->
                        importes[moneda] = importes[moneda]?.plus(importe) ?: importe
                    }
                }
                ans.add(PagoInfo(categoria, categoria.stringName, importes = importes))
            }
        }
        return ans
    }

    private fun calculateGastosTotal(): PagoInfo {
        val importes = mutableMapOf<Moneda, Float>()
        for (pagoInfo in state.value.gastosSubtotals) {
            pagoInfo.importes.forEach { (moneda, importe) ->
                importes[moneda] = importes[moneda]?.plus(importe) ?: importe
            }
        }
        return PagoInfo(descripcion = "TOTAL", importes = importes)
    }

    fun onEvent(event: PagosEvent) {
        when (event) {
            PagosEvent.HideCobrosDetail -> {
                _state.value = state.value.copy(
                    showCobrosDetail = false
                )
            }
            PagosEvent.HideGastosDetail -> {
                _state.value = state.value.copy(
                    showGastosDetail = false
                )
            }
            PagosEvent.HideSettingsDialog -> {
                _state.value = state.value.copy(
                    showSettingsDialog = false
                )
            }
            PagosEvent.NextPeriod -> {
                viewModelScope.launch {
                    setLoading()
                    when (state.value.dateGroupCriteria) {
                        DateGroupCriteria.BY_MONTH -> {
                            _state.value = state.value.copy(
                                yearMonth = state.value.yearMonth.plusMonths(1)
                            )
                        }
                        DateGroupCriteria.BY_TEMPORADA -> {
                            _state.value = state.value.copy(
                                temporada = Temporada(state.value.temporada.firstYear.plusYears(1))
                            )
                        }
                        DateGroupCriteria.CUSTOM -> {

                        }
                    }
                    getCobros()
                    getGastos()
                }
            }
            PagosEvent.PreviousPeriod -> {
                viewModelScope.launch {
                    setLoading()
                    when (state.value.dateGroupCriteria) {
                        DateGroupCriteria.BY_MONTH -> {
                            _state.value = state.value.copy(
                                yearMonth = state.value.yearMonth.minusMonths(1)
                            )
                        }
                        DateGroupCriteria.BY_TEMPORADA -> {
                            _state.value = state.value.copy(
                                temporada = Temporada(state.value.temporada.firstYear.minusYears(1))
                            )
                        }
                        DateGroupCriteria.CUSTOM -> {

                        }
                    }
                    getCobros()
                    getGastos()
                }

            }
            PagosEvent.ShowCobrosDetail -> {
                _state.value = state.value.copy(
                    showCobrosDetail = true
                )
            }
            PagosEvent.ShowGastosDetail -> {
                _state.value = state.value.copy(
                    showGastosDetail = true
                )
            }
            PagosEvent.ShowSettingsDialog -> {
                _state.value = state.value.copy(
                    showSettingsDialog = true
                )
            }
            is PagosEvent.SetDateDefinitionCriteria -> {
                viewModelScope.launch {
                    _state.value = state.value.copy(
                        cobrosLoadingInfo = LoadingInfo(LoadingState.LOADING),
                        dateDefinitionCriteria = event.definitionCriteria
                    )
                    getCobros()
                }
            }
            is PagosEvent.SetDateGroupCriteria -> {
                viewModelScope.launch {
                    setLoading()
                    _state.value = state.value.copy(
                        dateGroupCriteria = event.groupCriteria
                    )
                    getCobros()
                    getGastos()
                }
            }
            is PagosEvent.SetCustomPeriod -> {
                viewModelScope.launch {
                    setLoading()
                    _state.value = state.value.copy(
                        customPeriod = event.customPeriod
                    )
                    getCobros()
                    getGastos()
                }
            }
        }
    }

    private suspend fun getCobros() {
        getCobrosJob?.cancelAndJoin()
        val dateInterval = state.value.getDateInterval()
        getCobrosJob = viewModelScope.launch {
            val cobros: List<Cobro> = when (state.value.dateDefinitionCriteria) {
                DateDefinitionCriteria.BY_PAYMENT_DATE -> {
                    cobroUseCases.getCobrosByPaymentDate(dateInterval.first, dateInterval.second)
                }
                DateDefinitionCriteria.BY_RESERVA_DATE -> {
                    cobroUseCases.getCobrosByReservaDate(
                        dateInterval.first,
                        dateInterval.second
                    )
                }
            }
            val map = mutableMapOf<Casa, MutableList<PagoInfo>>()
            for (cobro in cobros) {
                val reserva = reservaUseCases.getReserva(cobro.reservaId)
                val casa = reserva?.casa
                if (casa != null) {
                    map.putIfAbsent(casa, mutableListOf())
                    val descripcion =
                        "${
                            clienteUseCases.getCliente(reserva.clienteId)
                                ?.getNombreCompleto() ?: ""
                        }\n${reserva.getRangoDeFechasString("dd/MM", ">")}"
                    map[casa]!!.add(
                        PagoInfo(
                            element = cobro,
                            descripcion = descripcion,
                            fecha = cobro.fechaPago,
                            importes = mapOf(Pair(cobro.moneda, cobro.importe))
                        )
                    )
                }
            }
            _state.value = state.value.copy(
                cobros = map,
            )
            val subtotals = calculateCobrosSubtotals()
            _state.value = state.value.copy(
                cobrosSubtotals = subtotals,
            )
            val total = calculateCobrosTotal()
            _state.value = state.value.copy(
                cobrosTotal = total,
                cobrosLoadingInfo = LoadingInfo(LoadingState.READY)
            )
        }
    }

    private suspend fun getGastos() {
        getGastosJob?.cancelAndJoin()
        val dateInterval = state.value.getDateInterval()
        getGastosJob = viewModelScope.launch {
            gastoUseCases.getGastosInRange(dateInterval.first, dateInterval.second)
                .let { gastos ->
                    val map = mutableMapOf<Categoria, MutableList<PagoInfo>>()
                    for (gasto in gastos) {
                        map.putIfAbsent(gasto.categoria, mutableListOf())
                        val descripcion =
                            "${gasto.categoria} ${if (!gasto.descripcion.isNullOrBlank()) ": " + gasto.descripcion else ""}"
                        map[gasto.categoria]!!.add(
                            PagoInfo(
                                element = gasto,
                                descripcion = descripcion,
                                fecha = gasto.fecha,
                                importes = mapOf(Pair(gasto.moneda, gasto.importe))
                            )
                        )
                    }
                    _state.value = state.value.copy(
                        gastos = map,
                    )
                }
            val subtotals = calculateGastosSubtotals()
            _state.value = state.value.copy(
                gastosSubtotals = subtotals,
            )
            val total = calculateGastosTotal()
            _state.value = state.value.copy(
                gastosTotal = total,
                gastosLoadingInfo = LoadingInfo(LoadingState.READY)
            )
        }
    }
}