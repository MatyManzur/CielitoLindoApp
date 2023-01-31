package com.example.cielitolindo.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class Cobro(
    @PrimaryKey val id: String,
    val clienteId: String,
    val reservaId: String,
    val fechaPago: LocalDate,
    val modoPago: String?,
    val descripcion: String?,
    val importe: Float,
    val moneda: Moneda
) : Element

class InvalidCobroException(message: String) : Exception(message)