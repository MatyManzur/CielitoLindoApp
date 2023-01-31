package com.example.cielitolindo.domain.repository

import com.example.cielitolindo.domain.model.Cobro
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface CobroRepository {
    fun getAllCobros(): Flow<List<Cobro>>
    suspend fun getCobrosInRange(dateFrom: LocalDate, dateTo: LocalDate): Flow<List<Cobro>>
    suspend fun getCobroById(id: String): Cobro?
    suspend fun getCobrosFromCliente(clienteId: String): Flow<List<Cobro>>
    suspend fun getCobrosFromReserva(reservaId: String): Flow<List<Cobro>>
    suspend fun insertCobro(cobro: Cobro)
    suspend fun deleteCobro(cobro: Cobro)
    suspend fun deleteAllCobros()
}