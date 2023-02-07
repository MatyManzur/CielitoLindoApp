package com.example.cielitolindo.domain.use_case.gastos

import com.example.cielitolindo.domain.repository.FirestoreRepository
import com.example.cielitolindo.domain.repository.GastoRepository

class FetchGastos(
    private val gastoRepository: GastoRepository,
    private val firestoreRepository: FirestoreRepository
) {
    suspend operator fun invoke() {
        val allNewGastos = firestoreRepository.getGastos()
        gastoRepository.deleteAllGastos()
        allNewGastos.forEach { gasto ->
            gastoRepository.insertGasto(gasto)
        }
    }
}