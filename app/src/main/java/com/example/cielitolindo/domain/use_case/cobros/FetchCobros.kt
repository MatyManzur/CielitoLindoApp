package com.example.cielitolindo.domain.use_case.cobros

import com.example.cielitolindo.domain.repository.CobroRepository
import com.example.cielitolindo.domain.repository.FirestoreRepository

class FetchCobros(
    private val cobroRepository: CobroRepository,
    private val firestoreRepository: FirestoreRepository
) {
    suspend operator fun invoke() {
        val allNewCobros = firestoreRepository.getCobros()
        cobroRepository.deleteAllCobros()
        allNewCobros.forEach { cobro ->
            cobroRepository.insertCobro(cobro)
        }
    }
}