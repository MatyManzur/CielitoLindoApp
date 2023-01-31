package com.example.cielitolindo.domain.repository

import com.example.cielitolindo.domain.model.Reserva
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ReservaRepository {
    fun getAllReservas(): Flow<List<Reserva>>
    suspend fun getReservasInRange(dateFrom: LocalDate, dateTo: LocalDate): Flow<List<Reserva>>
    suspend fun getReservaById(id: String): Reserva?
    suspend fun getReservasFromCliente(clienteId: String): Flow<List<Reserva>>
    suspend fun insertReserva(reserva: Reserva)
    suspend fun deleteReserva(reserva: Reserva)
    suspend fun deleteAllReservas()
}