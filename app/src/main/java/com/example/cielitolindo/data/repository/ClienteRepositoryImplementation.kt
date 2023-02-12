package com.example.cielitolindo.data.repository

import com.example.cielitolindo.data.data_source.ClienteDao
import com.example.cielitolindo.domain.model.Cliente
import com.example.cielitolindo.domain.repository.ClienteRepository
import kotlinx.coroutines.flow.Flow

class ClienteRepositoryImplementation(private val clienteDao: ClienteDao) : ClienteRepository {
    override suspend fun getClientes(): List<Cliente> {
        return clienteDao.getClientes()
    }

    override suspend fun getClienteById(id: String): Cliente? {
        return clienteDao.getClienteById(id)
    }

    override suspend fun insertCliente(cliente: Cliente) {
        clienteDao.insertCliente(cliente)
    }

    override suspend fun deleteCliente(cliente: Cliente) {
        clienteDao.deleteCliente(cliente)
    }

    override suspend fun deleteAllClientes() {
        clienteDao.deleteAllClientes()
    }
}