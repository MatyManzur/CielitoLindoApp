package com.example.cielitolindo.domain.use_case.clientes

import com.example.cielitolindo.domain.model.Cliente
import com.example.cielitolindo.domain.model.InvalidClienteException
import com.example.cielitolindo.domain.repository.ClienteRepository
import com.example.cielitolindo.domain.repository.FirestoreRepository

class AddCliente(
    private val clienteRepository: ClienteRepository,
    private val firestoreRepository: FirestoreRepository
) {
    @Throws(InvalidClienteException::class)
    suspend operator fun invoke(cliente: Cliente, onFirebaseSuccessListener: () -> Unit, onFirebaseFailureListener: (Exception) -> Unit) {
        if (cliente.nombre.isBlank()) {
            throw InvalidClienteException("El nombre del cliente no puede estar vacío.")
        }
        clienteRepository.insertCliente(cliente)
        try {
            firestoreRepository.setCliente(cliente, onFirebaseSuccessListener, onFirebaseFailureListener)
        } catch (ex: Exception) {
            onFirebaseFailureListener(ex)
        }
    }
}