package com.example.cielitolindo.domain.repository

import com.example.cielitolindo.domain.model.Gasto
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface GastoRepository {
    suspend fun getGastosInRange(dateFrom: LocalDate, dateTo: LocalDate): List<Gasto>
    suspend fun getGastoById(id: String): Gasto?
    suspend fun insertGasto(gasto: Gasto)
    suspend fun deleteGasto(gasto: Gasto)
    suspend fun deleteAllGastos()
}