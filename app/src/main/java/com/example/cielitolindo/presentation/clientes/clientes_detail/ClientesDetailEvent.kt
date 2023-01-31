package com.example.cielitolindo.presentation.clientes.clientes_detail

sealed class ClientesDetailEvent {
    object OnEdit : ClientesDetailEvent()
    object OnShowDeleteConfirmationDialog : ClientesDetailEvent()
    object OnHideDeleteConfirmationDialog : ClientesDetailEvent()
    object OnDelete : ClientesDetailEvent()
}