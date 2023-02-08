package com.example.cielitolindo.domain.use_case.gastos

import com.example.cielitolindo.domain.model.Gasto
import com.example.cielitolindo.domain.model.InvalidGastoException
import com.example.cielitolindo.domain.repository.FirestoreRepository
import com.example.cielitolindo.domain.repository.GastoRepository

class AddGasto(
    private val gastoRepository: GastoRepository,
    private val firestoreRepository: FirestoreRepository
) {
    @Throws(InvalidGastoException::class)
    suspend operator fun invoke(
        gasto: Gasto,
        onFirebaseSuccessListener: () -> Unit,
        onFirebaseFailureListener: (Exception) -> Unit
    ) {
        gastoRepository.insertGasto(gasto)
        try {
            firestoreRepository.setGasto(
                gasto,
                onFirebaseSuccessListener,
                onFirebaseFailureListener
            )
        } catch (e: Exception) {
            onFirebaseFailureListener(e)
        }
    }
}