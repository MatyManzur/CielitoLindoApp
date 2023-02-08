package com.example.cielitolindo.domain.use_case.reservas

import com.example.cielitolindo.domain.model.Reserva
import com.example.cielitolindo.domain.repository.CobroRepository
import com.example.cielitolindo.domain.repository.FirestoreRepository
import com.example.cielitolindo.domain.repository.ReservaRepository
import kotlinx.coroutines.flow.firstOrNull

class DeleteReserva(
    private val reservaRepository: ReservaRepository,
    private val cobroRepository: CobroRepository,
    private val firestoreRepository: FirestoreRepository
) {
    suspend operator fun invoke(
        reserva: Reserva,
        onFirebaseSuccessListener: () -> Unit,
        onFirebaseFailureListener: (Exception) -> Unit
    ) {
        if (cobroRepository.getCobrosFromReserva(reserva.id).firstOrNull()?.isNotEmpty() == true) {
            throw IllegalStateException("No se puede eliminar una reserva mientras tenga cobros asociados!")
        }
        reservaRepository.deleteReserva(reserva)
        try {
            firestoreRepository.deleteReserva(
                reserva,
                onFirebaseSuccessListener,
                onFirebaseFailureListener
            )
        } catch (e: Exception) {
            onFirebaseFailureListener(e)
        }
    }
}