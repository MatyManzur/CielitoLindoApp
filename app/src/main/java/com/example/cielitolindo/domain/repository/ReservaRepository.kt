package com.example.cielitolindo.domain.repository

import com.example.cielitolindo.domain.model.Casa
import com.example.cielitolindo.domain.model.Reserva
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ReservaRepository {
    suspend fun getAllReservas(): List<Reserva>
    suspend fun getReservasInRange(dateFrom: LocalDate, dateTo: LocalDate): List<Reserva>
    suspend fun getReservaById(id: String): Reserva?
    suspend fun getReservasFromCliente(clienteId: String): List<Reserva>
    suspend fun countReservasOfCasaInRange(casa: Casa, dateFrom: LocalDate, dateTo: LocalDate): Int
    suspend fun insertReserva(reserva: Reserva)
    suspend fun deleteReserva(reserva: Reserva)
    suspend fun deleteAllReservas()
}