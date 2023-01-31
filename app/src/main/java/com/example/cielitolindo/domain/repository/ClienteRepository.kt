package com.example.cielitolindo.domain.repository

import com.example.cielitolindo.domain.model.Cliente
import kotlinx.coroutines.flow.Flow

interface ClienteRepository {
    fun getClientes(): Flow<List<Cliente>>
    suspend fun getClienteById(id: String): Cliente?
    suspend fun insertCliente(cliente: Cliente)
    suspend fun deleteCliente(cliente: Cliente)
    suspend fun deleteAllClientes()
}