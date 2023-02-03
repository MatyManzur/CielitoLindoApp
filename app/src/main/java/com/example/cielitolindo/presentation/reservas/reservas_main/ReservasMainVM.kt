package com.example.cielitolindo.presentation.reservas.reservas_main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cielitolindo.domain.model.Casa
import com.example.cielitolindo.domain.model.Reserva
import com.example.cielitolindo.domain.model.getNombreCompleto
import com.example.cielitolindo.domain.use_case.clientes.ClienteUseCases
import com.example.cielitolindo.domain.use_case.reservas.ReservaUseCases
import com.example.cielitolindo.presentation.util.LoadingInfo
import com.example.cielitolindo.presentation.util.LoadingState
import com.example.cielitolindo.presentation.util.MonthWeeks
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class ReservasMainVM @Inject constructor(
    private val clienteUseCases: ClienteUseCases,
    private val reservaUseCases: ReservaUseCases
) : ViewModel() {
    private val _state = mutableStateOf(ReservasState())
    val state: State<ReservasState> = _state

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

    private suspend fun getCountReservasOfCasaUntil(casa: Casa, until: LocalDate): Int {
        return reservaUseCases.countReservasOfCasaInRange(
            casa,
            LocalDate.of(until.minusMonths(6).year, Month.JULY, 1),
            until.minusDays(1)
        )
    }

    private fun nextCasa(casa: Casa?): Casa? {
        return when (casa) {
            Casa.CELESTE -> Casa.NARANJA
            Casa.NARANJA -> Casa.VERDE
            Casa.VERDE -> null
            null -> Casa.CELESTE
        }
    }

    private fun getReservasForMonthCasa(yearMonth: YearMonth, casa: Casa?) {
        val weeksOfMonth = MonthWeeks.getWeeksOfMonth(yearMonth.month, yearMonth.year)
        val emptyReservasWeekList: MutableList<ReservasWeek> = mutableListOf()
        for (week in weeksOfMonth) {
            emptyReservasWeekList.add(ReservasWeek(week = week))
        }
        _state.value = state.value.copy(
            yearMonth = yearMonth,
            activeCasa = casa,
            loadingInfo = LoadingInfo(LoadingState.LOADING),
            reservasWeeks = emptyReservasWeekList
        )
        for ((i, week) in weeksOfMonth.withIndex()) {
            val reservasInWeekForCasa: MutableList<ReservaInfo> = mutableListOf()
            reservaUseCases.getReservasInRange(week.first(), week.last())
                .onEach { reservas ->
                    val reservasCount: MutableMap<Casa, Int> = mutableMapOf()
                    for (c in Casa.values()) {
                        if (casa == null || c == casa)
                            reservasCount[c] = getCountReservasOfCasaUntil(c, week.first())
                    }
                    reservasInWeekForCasa.addAll(reservas
                        .filter { r -> casa == null || r.casa == casa }
                        .map { r ->
                            ReservaInfo(
                                reserva = r,
                                clienteName = getNameOfCliente(r.clienteId),
                                ordinal = reservasCount[r.casa]?.also {
                                    reservasCount[r.casa] = reservasCount[r.casa]?.plus(1) ?: 1
                                } ?: 0)
                        })
                    _state.value.reservasWeeks[i] =
                        ReservasWeek(week = week, reservasInWeek = reservasInWeekForCasa)
                    _state.value = state.value.copy(
                        loadingInfo = LoadingInfo(LoadingState.READY),
                        reservasWeeks = state.value.reservasWeeks
                    )
                }
                .launchIn(viewModelScope)
        }
    }
}