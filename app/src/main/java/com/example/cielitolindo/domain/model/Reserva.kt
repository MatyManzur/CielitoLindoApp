package com.example.cielitolindo.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Entity
data class Reserva(
    @PrimaryKey val id: String,
    val clienteId: String,
    val casa: Casa,
    val fechaIngreso: LocalDate,
    val observacionesIngreso: String?,
    val fechaEgreso: LocalDate,
    val observacionesEgreso: String?,
    val importeTotal: Float,
    val moneda: Moneda,
    val observaciones: String?,
) : Element

fun Reserva.getShortRangoDeFechas(): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM")
    return String.format("%s al %s", fechaIngreso.format(formatter), fechaEgreso.format(formatter))
}

fun Reserva.getLongRangoDeFechas(): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yy")
    return String.format("%s -> %s", fechaIngreso.format(formatter), fechaEgreso.format(formatter))
}

class InvalidReservaException(message: String) : Exception(message)
