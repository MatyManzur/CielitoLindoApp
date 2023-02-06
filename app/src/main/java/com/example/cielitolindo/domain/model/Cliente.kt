package com.example.cielitolindo.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.Date

@Entity
data class Cliente(
    @PrimaryKey val id: String,
    val nombre: String,
    val apellido: String?,
    val fechaInscripcion: LocalDate,
    val dni: Int?,
    val direccion: String?,
    val localidad: String?,
    val provincia: String?,
    val telefono: String?,
    val email: String?,
    val observaciones: String?,
) : Element

fun Cliente.getNombreCompleto(): String {
    return "$nombre ${apellido ?: ""}"
}

fun Cliente.getDireccionCompleta(): String {
    var ans = ""
    if (!direccion.isNullOrBlank())
        ans = direccion
    if (!localidad.isNullOrBlank())
        ans = "$ans${if (ans.isNotBlank()) ", " else ""}$localidad"
    if (!provincia.isNullOrBlank())
        ans = "$ans${if (ans.isNotBlank()) ", " else ""}$provincia"
    return ans
}

class InvalidClienteException(message: String) : Exception(message)
