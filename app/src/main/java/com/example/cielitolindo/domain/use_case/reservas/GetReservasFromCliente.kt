package com.example.cielitolindo.domain.use_case.reservas

import com.example.cielitolindo.domain.model.Reserva
import com.example.cielitolindo.domain.repository.ReservaRepository
import kotlinx.coroutines.flow.Flow

class GetReservasFromCliente(private val reservaRepository: ReservaRepository) {
    suspend operator fun invoke(clienteId: String): List<Reserva> = reservaRepository.getReservasFromCliente(clienteId)
}