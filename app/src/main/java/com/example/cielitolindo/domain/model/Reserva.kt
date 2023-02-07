package com.example.cielitolindo.domain.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
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

private fun colorVariation(original: Float, hash: Int, seed: Int, maxVariation: Float = .1f) : Float {
    val ans = ((((seed * hash) % 100) / 100f) * 2 * maxVariation) - maxVariation
    if (original.plus(ans) > 1f) return 1f
    if (original.plus(ans) < 0f) return 0f
    return original.plus(ans)
}

@Composable
fun Reserva.getColor(): Color {
    val baseColor = casa.getSecondColor()
    return baseColor.copy(
        red = colorVariation(baseColor.red, id.hashCode(), 3),
        green = colorVariation(baseColor.green, id.hashCode(), 9),
        blue = colorVariation(baseColor.blue, id.hashCode(), 7)
    )
}

class InvalidReservaException(message: String) : Exception(message)
