package com.example.cielitolindo.domain.use_case.cobros

import com.example.cielitolindo.domain.model.Cobro
import com.example.cielitolindo.domain.repository.CobroRepository
import com.example.cielitolindo.domain.repository.ReservaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.time.LocalDate

class GetCobrosByReservaDate(private val cobroRepository: CobroRepository, private val reservaRepository: ReservaRepository) {
    suspend operator fun invoke(dateFrom: LocalDate, dateTo: LocalDate): List<Cobro> {
        val reservas = reservaRepository.getReservasInRange(dateFrom, dateTo)
        val ans: MutableList<Cobro> = mutableListOf()
        for (reserva in reservas) {
            if(!reserva.fechaIngreso.isBefore(dateFrom)) //para que no se sumen dos veces las reservas que se extienden de un mes a otro
                ans.addAll(cobroRepository.getCobrosFromReserva(reserva.id))
        }
        return ans
    }
}