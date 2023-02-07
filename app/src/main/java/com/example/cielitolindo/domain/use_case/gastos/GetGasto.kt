package com.example.cielitolindo.domain.use_case.gastos

import com.example.cielitolindo.domain.model.Gasto
import com.example.cielitolindo.domain.repository.GastoRepository

class GetGasto(
    private val gastoRepository: GastoRepository
) {
    suspend operator fun invoke(id: String): Gasto? = gastoRepository.getGastoById(id)
}