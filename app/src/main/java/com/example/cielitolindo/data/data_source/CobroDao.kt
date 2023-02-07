package com.example.cielitolindo.data.data_source

import androidx.room.*
import com.example.cielitolindo.domain.model.Cobro
import com.example.cielitolindo.domain.model.Reserva
import kotlinx.coroutines.flow.Flow

@Dao
interface CobroDao {
    @Query("SELECT * FROM cobro WHERE fechaPago >= :dateFrom AND fechaPago <= :dateTo")
    fun getCobrosByPaymentDate(dateFrom: String, dateTo: String) : Flow<List<Cobro>>

    @Query("SELECT * FROM cobro WHERE id = :id")
    suspend fun getCobroById(id: String): Cobro?

    @Query("SELECT * FROM cobro WHERE clienteId = :clienteId")
    fun getCobrosFromCliente(clienteId: String): Flow<List<Cobro>>

    @Query("SELECT * FROM cobro WHERE reservaId = :reservaId")
    fun getCobrosFromReserva(reservaId: String): Flow<List<Cobro>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCobro(cobro: Cobro)

    @Delete
    suspend fun deleteCobro(cobro: Cobro)

    @Query("DELETE FROM cobro")
    suspend fun deleteAllCobros()
}