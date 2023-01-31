package com.example.cielitolindo.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cielitolindo.domain.model.Cliente
import kotlinx.coroutines.flow.Flow

@Dao
interface ClienteDao {
    @Query("SELECT * FROM cliente")
    fun getClientes(): Flow<List<Cliente>>

    @Query("SELECT * FROM cliente WHERE id = :id")
    suspend fun getClienteById(id: String): Cliente?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCliente(cliente: Cliente)

    @Delete
    suspend fun deleteCliente(cliente: Cliente)

    @Query("DELETE FROM cliente")
    suspend fun deleteAllClientes()
}