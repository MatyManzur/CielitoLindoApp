package com.example.cielitolindo.domain.use_case.reservas

import com.example.cielitolindo.domain.model.*
import com.example.cielitolindo.domain.repository.ClienteRepository
import com.example.cielitolindo.domain.repository.FirestoreRepository
import com.example.cielitolindo.domain.repository.ReservaRepository
import kotlinx.coroutines.flow.onEach

class AddReserva(
    private val reservaRepository: ReservaRepository,
    private val clienteRepository: ClienteRepository,
    private val firestoreRepository: FirestoreRepository
) {
    @Throws(InvalidReservaException::class)
    suspend operator fun invoke(
        reserva: Reserva,
        onFirebaseSuccessListener: () -> Unit,
        onFirebaseFailureListener: (Exception) -> Unit
    ) {
        if (reserva.clienteId.isBlank()) {
            throw InvalidReservaException("Debe seleccionar un cliente!")
        } else {
            if (clienteRepository.getClienteById(reserva.clienteId) == null) {
                throw InvalidReservaException("El cliente no existe!")
            }
        }
        reservaRepository.getReservasInRange(reserva.fechaIngreso, reserva.fechaEgreso)
            .onEach { list ->
                list.forEach {
                    if (it.casa == reserva.casa) {
                        throw InvalidReservaException("Casa ocupada en esas fechas!")
                    }
                }
            }
        reservaRepository.insertReserva(reserva)
        try {
            firestoreRepository.setReserva(
                reserva,
                onFirebaseSuccessListener,
                onFirebaseFailureListener
            )
        } catch (e: Exception) {
            onFirebaseFailureListener(e)
        }

    }
}