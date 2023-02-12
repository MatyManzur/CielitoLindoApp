package com.example.cielitolindo.domain.repository

import com.example.cielitolindo.domain.model.Cobro
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface CobroRepository {
    suspend fun getCobrosByPaymentDate(dateFrom: LocalDate, dateTo: LocalDate) : List<Cobro>
    suspend fun getCobroById(id: String): Cobro?
    suspend fun getCobrosFromCliente(clienteId: String): List<Cobro>
    suspend fun getCobrosFromReserva(reservaId: String): List<Cobro>
    suspend fun insertCobro(cobro: Cobro)
    suspend fun deleteCobro(cobro: Cobro)
    suspend fun deleteAllCobros()
}