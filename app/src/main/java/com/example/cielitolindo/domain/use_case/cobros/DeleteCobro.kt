package com.example.cielitolindo.domain.use_case.cobros

import com.example.cielitolindo.domain.model.Cobro
import com.example.cielitolindo.domain.model.Reserva
import com.example.cielitolindo.domain.repository.CobroRepository
import com.example.cielitolindo.domain.repository.FirestoreRepository

class DeleteCobro(
    private val cobroRepository: CobroRepository,
    private val firestoreRepository: FirestoreRepository
) {
    suspend operator fun invoke(cobro: Cobro, onFirebaseSuccessListener: () -> Unit, onFirebaseFailureListener: (Exception) -> Unit) {
        cobroRepository.deleteCobro(cobro)
        try {
            firestoreRepository.deleteCobro(cobro, onFirebaseSuccessListener, onFirebaseFailureListener)
        } catch (e: Exception) {
            onFirebaseFailureListener(e)
        }
    }
}