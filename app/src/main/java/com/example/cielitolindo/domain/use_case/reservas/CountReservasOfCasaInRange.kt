package com.example.cielitolindo.domain.use_case.reservas

import com.example.cielitolindo.domain.model.Casa
import com.example.cielitolindo.domain.model.Reserva
import com.example.cielitolindo.domain.repository.ReservaRepository
import java.time.LocalDate

class CountReservasOfCasaInRange(
    private val reservaRepository: ReservaRepository
) {
    suspend operator fun invoke(casa: Casa, dateFrom: LocalDate, dateTo: LocalDate): Int =
        reservaRepository.countReservasOfCasaInRange(casa, dateFrom, dateTo)
}