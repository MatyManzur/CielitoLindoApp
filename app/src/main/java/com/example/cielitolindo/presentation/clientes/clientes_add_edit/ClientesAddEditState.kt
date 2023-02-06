package com.example.cielitolindo.presentation.clientes.clientes_add_edit

import com.example.cielitolindo.presentation.util.LoadingInfo
import com.example.cielitolindo.presentation.util.TextFieldState
import java.time.LocalDate
import java.util.*

data class ClientesAddEditState(
    val screenTitle: String = "Nuevo Cliente",
    val loadingInfo: LoadingInfo = LoadingInfo(),
    val id: String = UUID.randomUUID().toString(),
    val nombre: TextFieldState = TextFieldState(label = "Nombre"),
    val apellido: TextFieldState = TextFieldState(label = "Apellido"),
    val fechaInscripcion: LocalDate = LocalDate.now(),
    val dni: TextFieldState = TextFieldState(label = "DNI"),
    val direccion: TextFieldState = TextFieldState(label = "Direccion"),
    val localidad: TextFieldState = TextFieldState(label = "Localidad"),
    val provincia: TextFieldState = TextFieldState(label = "Provincia"),
    val telefono: TextFieldState = TextFieldState(label = "Telefono"),
    val email: TextFieldState = TextFieldState(label = "Email"),
    val observaciones: TextFieldState = TextFieldState(label = "Observaciones"),
) {
    val isSaveEnabled: Boolean
        get() = nombre.text.isNotBlank()
}