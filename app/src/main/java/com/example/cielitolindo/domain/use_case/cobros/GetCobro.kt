package com.example.cielitolindo.domain.use_case.cobros

import com.example.cielitolindo.domain.model.Cobro
import com.example.cielitolindo.domain.repository.CobroRepository

class GetCobro(
    private val cobroRepository: CobroRepository
) {
    suspend operator fun invoke(id: String): Cobro? = cobroRepository.getCobroById(id)
}