package com.example.cielitolindo.domain.use_case.reservas

import com.example.cielitolindo.domain.repository.FirestoreRepository
import com.example.cielitolindo.domain.repository.ReservaRepository

class FetchReservas(
    private val reservaRepository: ReservaRepository,
    private val firestoreRepository: FirestoreRepository
) {
    suspend operator fun invoke() {
        val allNewReservas = firestoreRepository.getReservas()
        reservaRepository.deleteAllReservas()
        allNewReservas.forEach { reserva ->
            reservaRepository.insertReserva(reserva)
        }
    }
}