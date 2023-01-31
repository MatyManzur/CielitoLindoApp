package com.example.cielitolindo.data.repository

import com.example.cielitolindo.data.data_source.ReservaDao
import com.example.cielitolindo.domain.model.Reserva
import com.example.cielitolindo.domain.repository.ReservaRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ReservaRepositoryImplementation(private val reservaDao: ReservaDao) : ReservaRepository {
    override fun getAllReservas(): Flow<List<Reserva>> {
        return reservaDao.getAllReservas()
    }

    override suspend fun getReservasInRange(dateFrom: LocalDate, dateTo: LocalDate): Flow<List<Reserva>> {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return reservaDao.getReservasInRange(dateFrom.format(formatter), dateTo.format(formatter))
    }

    override suspend fun getReservaById(id: String): Reserva? {
        return reservaDao.getReservaById(id)
    }

    override suspend fun getReservasFromCliente(clienteId: String): Flow<List<Reserva>> {
        return reservaDao.getReservasFromCliente(clienteId)
    }

    override suspend fun insertReserva(reserva: Reserva) {
        return reservaDao.insertReserva(reserva)
    }

    override suspend fun deleteReserva(reserva: Reserva) {
        return reservaDao.deleteReserva(reserva)
    }

    override suspend fun deleteAllReservas() {
        return reservaDao.deleteAllReservas()
    }

}