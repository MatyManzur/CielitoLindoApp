package com.example.cielitolindo.data.data_source

import androidx.room.*
import com.example.cielitolindo.domain.model.Gasto
import kotlinx.coroutines.flow.Flow

@Dao
interface GastoDao {
    @Query("SELECT * FROM gasto WHERE fecha >= :dateFrom AND fecha <= :dateTo")
    suspend fun getGastosInRange(dateFrom: String, dateTo: String): List<Gasto>

    @Query("SELECT * FROM gasto WHERE id = :id")
    suspend fun getGastoById(id: String): Gasto?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGasto(gasto: Gasto)

    @Delete
    suspend fun deleteGasto(gasto: Gasto)

    @Query("DELETE FROM gasto")
    suspend fun deleteAllGastos()
}