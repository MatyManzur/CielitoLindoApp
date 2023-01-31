package com.example.cielitolindo.domain.use_case.clientes

import com.example.cielitolindo.domain.model.Cliente
import com.example.cielitolindo.domain.model.PendingOps
import com.example.cielitolindo.domain.repository.ClienteRepository
import com.example.cielitolindo.domain.repository.FirestoreRepository
import com.example.cielitolindo.domain.repository.ReservaRepository
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onEmpty

class DeleteCliente(
    private val clienteRepository: ClienteRepository,
    private val reservaRepository: ReservaRepository,
    private val firestoreRepository: FirestoreRepository
) {
    suspend operator fun invoke(cliente: Cliente, onFirebaseSuccessListener: () -> Unit, onFirebaseFailureListener: (Exception) -> Unit) {
        if(reservaRepository.getReservasFromCliente(cliente.id).firstOrNull()?.isNotEmpty() == true) {
            throw IllegalStateException("No se puede eliminar un cliente mientras tenga reservas a su nombre!")
        }
        clienteRepository.deleteCliente(cliente)
        try {
            firestoreRepository.deleteCliente(cliente, onFirebaseSuccessListener, onFirebaseFailureListener)
        } catch (ex: Exception) {
            onFirebaseFailureListener(ex)
        }
    }
}