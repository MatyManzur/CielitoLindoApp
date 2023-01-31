package com.example.cielitolindo.domain.use_case.reservas

import com.example.cielitolindo.domain.model.Cliente
import com.example.cielitolindo.domain.model.PendingOps
import com.example.cielitolindo.domain.model.Reserva
import com.example.cielitolindo.domain.repository.FirestoreRepository
import com.example.cielitolindo.domain.repository.ReservaRepository
import kotlinx.coroutines.flow.count

class DeleteReserva(
    private val reservaRepository: ReservaRepository,
    private val firestoreRepository: FirestoreRepository
) {
    suspend operator fun invoke(reserva: Reserva, onFirebaseSuccessListener: () -> Unit, onFirebaseFailureListener: (Exception) -> Unit) {
        reservaRepository.deleteReserva(reserva)
        try {
            firestoreRepository.deleteReserva(reserva, onFirebaseSuccessListener, onFirebaseFailureListener)
        } catch (e: Exception) {
            onFirebaseFailureListener(e)
        }
    }
}