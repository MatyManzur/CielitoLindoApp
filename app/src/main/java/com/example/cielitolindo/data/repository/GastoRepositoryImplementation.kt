package com.example.cielitolindo.data.repository

import com.example.cielitolindo.data.data_source.GastoDao
import com.example.cielitolindo.domain.model.Gasto
import com.example.cielitolindo.domain.repository.GastoRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class GastoRepositoryImplementation(private val gastoDao: GastoDao) : GastoRepository {

    override fun getGastosInRange(
        dateFrom: LocalDate,
        dateTo: LocalDate
    ): Flow<List<Gasto>> {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return gastoDao.getGastosInRange(dateFrom.format(formatter), dateTo.format(formatter))
    }

    override suspend fun getGastoById(id: String): Gasto? {
        return gastoDao.getGastoById(id)
    }

    override suspend fun insertGasto(gasto: Gasto) {
        return gastoDao.insertGasto(gasto)
    }

    override suspend fun deleteGasto(gasto: Gasto) {
        return gastoDao.deleteGasto(gasto)
    }

    override suspend fun deleteAllGastos() {
        return gastoDao.deleteAllGastos()
    }

}