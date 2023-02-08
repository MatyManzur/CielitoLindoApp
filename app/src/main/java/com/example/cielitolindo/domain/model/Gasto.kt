package com.example.cielitolindo.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class Gasto(
    @PrimaryKey val id: String,
    val fecha: LocalDate,
    val descripcion: String?,
    val categoria: Categoria,
    val importe: Float,
    val moneda: Moneda
) : Element

class InvalidGastoException(message: String) : Exception(message)