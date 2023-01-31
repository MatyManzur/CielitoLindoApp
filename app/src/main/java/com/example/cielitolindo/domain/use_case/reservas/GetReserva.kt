package com.example.cielitolindo.domain.use_case.reservas

import com.example.cielitolindo.domain.model.Reserva
import com.example.cielitolindo.domain.repository.ReservaRepository

class GetReserva(
    private val reservaRepository: ReservaRepository
) {
    suspend operator fun invoke(id: String): Reserva? = reservaRepository.getReservaById(id)
}