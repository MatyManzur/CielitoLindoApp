package com.example.cielitolindo.domain.use_case.clientes

import com.example.cielitolindo.domain.repository.ClienteRepository
import com.example.cielitolindo.domain.repository.FirestoreRepository

class FetchClientes (
    private val clienteRepository: ClienteRepository,
    private val firestoreRepository: FirestoreRepository
) {
    suspend operator fun invoke() {
        val allNewClientes = firestoreRepository.getClientes()
        clienteRepository.deleteAllClientes()
        allNewClientes.forEach { cliente ->
            clienteRepository.insertCliente(cliente)
        }
    }
}