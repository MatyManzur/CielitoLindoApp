package com.example.cielitolindo.domain.repository

import com.example.cielitolindo.domain.model.Gasto
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface GastoRepository {
    fun getAllGastos(): Flow<List<Gasto>>
    suspend fun getGastosInRange(dateFrom: LocalDate, dateTo: LocalDate): Flow<List<Gasto>>
    suspend fun getGastoById(id: String): Gasto?
    suspend fun getGastosFromCategoria(categoria: String): Flow<List<Gasto>>
    suspend fun insertGasto(gasto: Gasto)
    suspend fun deleteGasto(gasto: Gasto)
    suspend fun deleteAllGastos()
}