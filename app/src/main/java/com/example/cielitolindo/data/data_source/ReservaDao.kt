package com.example.cielitolindo.data.data_source

import androidx.room.*
import com.example.cielitolindo.domain.model.Cliente
import com.example.cielitolindo.domain.model.Reserva
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservaDao {
    @Query("SELECT * FROM reserva")
    fun getAllReservas(): Flow<List<Reserva>>

    @Query("SELECT * FROM reserva WHERE fechaEgreso >= :dateFrom AND fechaIngreso <= :dateTo")
    fun getReservasInRange(dateFrom: String, dateTo: String) : Flow<List<Reserva>>

    @Query("SELECT * FROM reserva WHERE id = :id")
    suspend fun getReservaById(id: String): Reserva?

    @Query("SELECT * FROM reserva WHERE clienteId = :clienteId")
    fun getReservasFromCliente(clienteId: String): Flow<List<Reserva>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReserva(reserva: Reserva)

    @Delete
    suspend fun deleteReserva(reserva: Reserva)

    @Query("DELETE FROM reserva")
    suspend fun deleteAllReservas()
}