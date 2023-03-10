package com.example.cielitolindo.domain.use_case.cobros

import com.example.cielitolindo.domain.model.Cobro
import com.example.cielitolindo.domain.repository.CobroRepository
import kotlinx.coroutines.flow.Flow

class GetCobrosFromReserva(private val cobroRepository: CobroRepository) {
    suspend operator fun invoke(reservaId: String): List<Cobro> = cobroRepository.getCobrosFromReserva(reservaId)
}