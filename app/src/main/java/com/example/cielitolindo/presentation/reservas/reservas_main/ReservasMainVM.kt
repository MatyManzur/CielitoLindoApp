package com.example.cielitolindo.presentation.reservas.reservas_main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cielitolindo.domain.model.Casa
import com.example.cielitolindo.domain.model.getNombreCompleto
import com.example.cielitolindo.domain.use_case.clientes.ClienteUseCases
import com.example.cielitolindo.domain.use_case.reservas.ReservaUseCases
import com.example.cielitolindo.presentation.util.LoadingInfo
import com.example.cielitolindo.presentation.util.LoadingState
import com.example.cielitolindo.presentation.util.MonthWeeks
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class ReservasMainVM @Inject constructor(
    private val clienteUseCases: ClienteUseCases,
    private val reservaUseCases: ReservaUseCases
) : ViewModel() {
    private val _state = mutableStateOf(ReservasState())
    val state: State<ReservasState> = _state

    private var getReservasJobs: MutableList<Job?> = mutableListOf(null, null, null, null, null, null)

    init {
        viewModelScope.launch {
            reservaUseCases.fetchReservas()
            getReservasForMonthCasa(state.value.yearMonth, state.value.activeCasa)
        }
    }

    fun onEvent(event: ReservasEvent) {
        when (event) {
            ReservasEvent.FetchReservas -> {
                viewModelScope.launch {
                    reservaUseCases.fetchReservas()
                    getReservasForMonthCasa(state.value.yearMonth, state.value.activeCasa)
                }
            }
            ReservasEvent.NextMonth -> {
                viewModelScope.launch {
                    reservaUseCases.fetchReservas()
                    getReservasForMonthCasa(
                        state.value.yearMonth.plusMonths(1),
                        state.value.activeCasa
                    )
                }
            }
            ReservasEvent.PreviousMonth -> {
                viewModelScope.launch {
                    reservaUseCases.fetchReservas()
                    getReservasForMonthCasa(
                        state.value.yearMonth.minusMonths(1),
                        state.value.activeCasa
                    )
                }
            }
            ReservasEvent.ToggleCasa -> {
                viewModelScope.launch {
                    reservaUseCases.fetchReservas()
                    getReservasForMonthCasa(state.value.yearMonth, nextCasa(state.value.activeCasa))
                }
            }
        }
    }

    private suspend fun getNameOfCliente(clienteId: String): String {
        return clienteUseCases.getCliente(clienteId)?.getNombreCompleto() ?: ""
    }

    private fun nextCasa(casa: Casa?): Casa? {
        return when (casa) {
            Casa.CELESTE -> Casa.NARANJA
            Casa.NARANJA -> Casa.VERDE
            Casa.VERDE -> null
            null -> Casa.CELESTE
        }
    }

    private suspend fun getReservasForMonthCasa(yearMonth: YearMonth, casa: Casa?) {
        val weeksOfMonth = MonthWeeks.getWeeksOfMonth(yearMonth.month, yearMonth.year)
        val emptyReservasWeekList: MutableList<List<ReservaInfo>> = mutableListOf()
        for (w in weeksOfMonth)
            emptyReservasWeekList.add(listOf())
        _state.value = state.value.copy(
            yearMonth = yearMonth,
            activeCasa = casa,
            loadingInfo = LoadingInfo(LoadingState.LOADING),
            weeks = weeksOfMonth,
            reservasWeeks = emptyReservasWeekList
        )
        for ((i, week) in state.value.weeks.withIndex()) {
            getReservasJobs[i]?.cancelAndJoin()
            val reservasInWeekForCasa: MutableList<ReservaInfo> = mutableListOf()
            val reservasInRange = reservaUseCases.getReservasInRange(week.first(), week.last())
            getReservasJobs[i] = reservasInRange.cancellable()
                .onEach { reservas ->
                    yield()
                    reservasInWeekForCasa.addAll(reservas
                        .filter { r -> casa == null || r.casa == casa }
                        .map { r ->
                            ReservaInfo(
                                reserva = r,
                                clienteName = getNameOfCliente(r.clienteId))
                        })
                    if(state.value.reservasWeeks.count() > i) //para que no crashee si apretas muy rapido
                        _state.value.reservasWeeks[i] = reservasInWeekForCasa
                    throw CancellationException()
                }
                .launchIn(viewModelScope)
        }
        for ((i, _) in state.value.weeks.withIndex()) {
            getReservasJobs[i]?.join()
        }
        _state.value = state.value.copy(
            loadingInfo = LoadingInfo(LoadingState.READY)
        )
    }
}