package com.example.cielitolindo.domain.use_case.gastos

import com.example.cielitolindo.domain.model.Gasto
import com.example.cielitolindo.domain.repository.GastoRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class GetGastosInRange(private val gastoRepository: GastoRepository) {
    operator fun invoke(dateFrom: LocalDate, dateTo: LocalDate): Flow<List<Gasto>> = gastoRepository.getGastosInRange(dateFrom, dateTo)
}