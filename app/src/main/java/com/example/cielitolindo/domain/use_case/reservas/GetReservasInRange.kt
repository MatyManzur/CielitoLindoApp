package com.example.cielitolindo.domain.use_case.reservas

import com.example.cielitolindo.domain.model.Reserva
import com.example.cielitolindo.domain.repository.ReservaRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class GetReservasInRange(private val reservaRepository: ReservaRepository) {
    suspend operator fun invoke(dateFrom: LocalDate, dateTo: LocalDate): List<Reserva> = reservaRepository.getReservasInRange(dateFrom, dateTo)
}