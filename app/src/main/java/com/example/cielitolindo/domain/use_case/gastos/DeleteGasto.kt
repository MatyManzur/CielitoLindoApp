package com.example.cielitolindo.domain.use_case.gastos

import com.example.cielitolindo.domain.model.Gasto
import com.example.cielitolindo.domain.repository.FirestoreRepository
import com.example.cielitolindo.domain.repository.GastoRepository

class DeleteGasto(
    private val gastoRepository: GastoRepository,
    private val firestoreRepository: FirestoreRepository
) {
    suspend operator fun invoke(gasto: Gasto, onFirebaseSuccessListener: () -> Unit, onFirebaseFailureListener: (Exception) -> Unit) {
        gastoRepository.deleteGasto(gasto)
        try {
            firestoreRepository.deleteGasto(gasto, onFirebaseSuccessListener, onFirebaseFailureListener)
        } catch (e: Exception) {
            onFirebaseFailureListener(e)
        }
    }
}