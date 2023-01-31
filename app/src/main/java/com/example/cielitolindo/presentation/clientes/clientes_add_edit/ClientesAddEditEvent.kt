package com.example.cielitolindo.presentation.clientes.clientes_add_edit

sealed class ClientesAddEditEvent {
    data class EnteredNombre(val value: String) : ClientesAddEditEvent()
    data class EnteredApellido(val value: String) : ClientesAddEditEvent()
    data class EnteredDni(val value: String) : ClientesAddEditEvent()
    data class EnteredDireccion(val value: String) : ClientesAddEditEvent()
    data class EnteredLocalidad(val value: String) : ClientesAddEditEvent()
    data class EnteredProvincia(val value: String) : ClientesAddEditEvent()
    data class EnteredTelefono(val value: String) : ClientesAddEditEvent()
    data class EnteredEmail(val value: String) : ClientesAddEditEvent()
    data class EnteredObservaciones(val value: String) : ClientesAddEditEvent()
    object OnSave : ClientesAddEditEvent()
    object OnDiscard : ClientesAddEditEvent()
}