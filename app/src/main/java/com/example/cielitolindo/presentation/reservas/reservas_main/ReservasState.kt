package com.example.cielitolindo.presentation.reservas.reservas_main

import com.example.cielitolindo.domain.model.Casa
import com.example.cielitolindo.domain.model.Reserva
import com.example.cielitolindo.presentation.util.LoadingInfo
import com.example.cielitolindo.presentation.util.LoadingState
import java.time.LocalDate
import java.time.YearMonth

data class ReservasState(
    val yearMonth: YearMonth = YearMonth.now(),
    val reservasWeeks: MutableList<ReservasWeek> = mutableListOf(),
    val activeCasa: Casa? = null,
    val loadingInfo: LoadingInfo = LoadingInfo(LoadingState.LOADING)
)

data class ReservasWeek(val week: List<LocalDate> = listOf(), val reservasInWeek: List<ReservaInfo> = listOf())

data class ReservaInfo(val reserva: Reserva, val clienteName: String, val ordinal: Int)