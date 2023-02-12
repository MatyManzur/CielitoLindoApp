package com.example.cielitolindo.domain.use_case.cobros

import com.example.cielitolindo.domain.model.Cobro
import com.example.cielitolindo.domain.model.Reserva
import com.example.cielitolindo.domain.repository.CobroRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class GetCobrosByPaymentDate(private val cobroRepository: CobroRepository) {
    suspend operator fun invoke(dateFrom: LocalDate, dateTo: LocalDate): List<Cobro> = cobroRepository.getCobrosByPaymentDate(dateFrom, dateTo)
}