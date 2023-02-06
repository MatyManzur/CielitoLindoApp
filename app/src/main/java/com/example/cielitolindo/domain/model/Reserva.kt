package com.example.cielitolindo.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Entity
data class Reserva(
    @PrimaryKey val id: String,
    val clienteId: String,
    val casa: Casa,
    val fechaIngreso: LocalDate,
    val fechaEgreso: LocalDate,
    val importeTotal: Float,
    val moneda: Moneda,
    val observaciones: String?,
) : Element

fun Reserva.getRangoDeFechasString(pattern: String = "dd/MM", connecting: String = "al"): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return "${fechaIngreso.format(formatter)} $connecting ${fechaEgreso.format(formatter)}"
}

class InvalidReservaException(message: String) : Exception(message)
