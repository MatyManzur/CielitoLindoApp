package com.example.cielitolindo.domain.use_case.cobros

import com.example.cielitolindo.domain.model.Cobro
import com.example.cielitolindo.domain.model.InvalidCobroException
import com.example.cielitolindo.domain.repository.CobroRepository
import com.example.cielitolindo.domain.repository.FirestoreRepository
import com.example.cielitolindo.domain.repository.ReservaRepository

class AddCobro(
    private val cobroRepository: CobroRepository,
    private val reservaRepository: ReservaRepository,
    private val firestoreRepository: FirestoreRepository
) {
    @Throws(InvalidCobroException::class)
    suspend operator fun invoke(
        cobro: Cobro,
        onFirebaseSuccessListener: () -> Unit,
        onFirebaseFailureListener: (Exception) -> Unit
    ) {
        if(cobro.reservaId.isBlank()) {
            throw InvalidCobroException("El cobro debe estar asociado a una reserva!")
        } else {
            val reserva = reservaRepository.getReservaById(cobro.reservaId)
                ?: throw InvalidCobroException("La reserva no existe!")
            if(reserva.clienteId != cobro.clienteId) {
                throw InvalidCobroException("El cobro debe estar asociado a una reserva del mismo cliente!")
            }
        }

        cobroRepository.insertCobro(cobro)
        try {
            firestoreRepository.setCobro(
                cobro,
                onFirebaseSuccessListener,
                onFirebaseFailureListener
            )
        } catch (e: Exception) {
            onFirebaseFailureListener(e)
        }
    }
}