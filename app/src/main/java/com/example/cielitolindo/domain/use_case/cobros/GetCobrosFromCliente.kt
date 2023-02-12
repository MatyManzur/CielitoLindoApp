package com.example.cielitolindo.domain.use_case.cobros

import com.example.cielitolindo.domain.model.Cobro
import com.example.cielitolindo.domain.repository.CobroRepository
import kotlinx.coroutines.flow.Flow

class GetCobrosFromCliente(private val cobroRepository: CobroRepository) {
    suspend operator fun invoke(clienteId: String): List<Cobro> = cobroRepository.getCobrosFromCliente(clienteId)
}