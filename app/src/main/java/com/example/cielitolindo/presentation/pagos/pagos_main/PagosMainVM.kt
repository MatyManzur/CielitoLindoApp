package com.example.cielitolindo.presentation.pagos.pagos_main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cielitolindo.domain.model.Casa
import com.example.cielitolindo.domain.model.Categoria
import com.example.cielitolindo.domain.model.getNombreCompleto
import com.example.cielitolindo.domain.model.getRangoDeFechasString
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
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

    suspend fun updatePagos() {
        _state.value = state.value.copy(
            loadingInfo = LoadingInfo(LoadingState.LOADING)
        )
        cobroUseCases.fetchCobros()
        gastoUseCases.fetchGastos()
        getCobros()
        getGastos()
    }

    fun onEvent(event: PagosEvent) {
        when(event) {
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
                    _state.value = state.value.copy(
                        loadingInfo = LoadingInfo(LoadingState.LOADING)
                    )
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
                    _state.value = state.value.copy(
                        loadingInfo = LoadingInfo(LoadingState.LOADING)
                    )
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
                        loadingInfo = LoadingInfo(LoadingState.LOADING)
                    )
                    _state.value = state.value.copy(
                        loadingInfo = LoadingInfo(LoadingState.LOADING)
                    )
                    _state.value = state.value.copy(
                        dateDefinitionCriteria = event.definitionCriteria
                    )
                    getCobros()
                }
            }
            is PagosEvent.SetDateGroupCriteria -> {
                viewModelScope.launch {
                    _state.value = state.value.copy(
                        loadingInfo = LoadingInfo(LoadingState.LOADING)
                    )
                    _state.value = state.value.copy(
                        dateGroupCriteria = event.groupCriteria
                    )
                    getCobros()
                    getGastos()
                }
            }
            is PagosEvent.SetCustomPeriod -> {
                viewModelScope.launch {
                    _state.value = state.value.copy(
                        loadingInfo = LoadingInfo(LoadingState.LOADING)
                    )
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
        when (state.value.dateDefinitionCriteria) {
            DateDefinitionCriteria.BY_PAYMENT_DATE -> {
                getCobrosJob =
                    cobroUseCases.getCobrosByPaymentDate(dateInterval.first, dateInterval.second)
                        .onEach { cobros ->
                            val map = mutableMapOf<Casa, MutableList<PagoInfo>>()
                            for (cobro in cobros) {
                                val reserva = reservaUseCases.getReserva(cobro.reservaId)
                                val casa = reserva?.casa
                                if (casa != null) {
                                    map.putIfAbsent(casa, mutableListOf())
                                    val descripcion =
                                        "${clienteUseCases.getCliente(reserva.clienteId)?.getNombreCompleto() ?: ""}\n${reserva.getRangoDeFechasString("dd/MM", ">")}"
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
                                loadingInfo = LoadingInfo(LoadingState.READY)
                            )
                        }.launchIn(viewModelScope)
            }
            DateDefinitionCriteria.BY_RESERVA_DATE -> {
                viewModelScope.launch {
                    val map = mutableMapOf<Casa, MutableList<PagoInfo>>()
                    val cobros = cobroUseCases.getCobrosByReservaDate(
                        dateInterval.first,
                        dateInterval.second
                    )
                    for (cobro in cobros) {
                        val reserva = reservaUseCases.getReserva(cobro.reservaId)
                        val casa = reserva?.casa
                        if (casa != null) {
                            map.putIfAbsent(casa, mutableListOf())
                            val descripcion =
                                "${clienteUseCases.getCliente(reserva.clienteId)?.getNombreCompleto() ?: ""}\n${reserva.getRangoDeFechasString("dd/MM", ">")}"
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
                        loadingInfo = LoadingInfo(LoadingState.READY)
                    )
                }
            }
        }
    }

    private suspend fun getGastos() {
        getGastosJob?.cancelAndJoin()
        val dateInterval = state.value.getDateInterval()
        getGastosJob = gastoUseCases.getGastosInRange(dateInterval.first, dateInterval.second)
            .onEach { gastos ->
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
                    loadingInfo = LoadingInfo(LoadingState.READY)
                )
            }.launchIn(viewModelScope)
    }
}