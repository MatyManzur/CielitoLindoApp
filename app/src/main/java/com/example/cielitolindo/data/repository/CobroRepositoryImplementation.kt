package com.example.cielitolindo.data.repository

import com.example.cielitolindo.data.data_source.CobroDao
import com.example.cielitolindo.domain.model.Cobro
import com.example.cielitolindo.domain.repository.CobroRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CobroRepositoryImplementation(private val cobroDao: CobroDao) : CobroRepository {
    override fun getAllCobros(): Flow<List<Cobro>> {
        return cobroDao.getAllCobros()
    }

    override suspend fun getCobrosInRange(
        dateFrom: LocalDate,
        dateTo: LocalDate
    ): Flow<List<Cobro>> {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return cobroDao.getCobrosInRange(dateFrom.format(formatter), dateTo.format(formatter))
    }

    override suspend fun getCobroById(id: String): Cobro? {
        return cobroDao.getCobroById(id)
    }

    override suspend fun getCobrosFromCliente(clienteId: String): Flow<List<Cobro>> {
       return cobroDao.getCobrosFromCliente(clienteId)
    }

    override suspend fun getCobrosFromReserva(reservaId: String): Flow<List<Cobro>> {
        return cobroDao.getCobrosFromReserva(reservaId)
    }

    override suspend fun insertCobro(cobro: Cobro) {
        return cobroDao.insertCobro(cobro)
    }

    override suspend fun deleteCobro(cobro: Cobro) {
        return cobroDao.deleteCobro(cobro)
    }

    override suspend fun deleteAllCobros() {
        return cobroDao.deleteAllCobros()
    }

}