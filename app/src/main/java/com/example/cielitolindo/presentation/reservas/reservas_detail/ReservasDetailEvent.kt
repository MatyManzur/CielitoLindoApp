package com.example.cielitolindo.presentation.reservas.reservas_detail

sealed class ReservasDetailEvent {
    object OnEdit : ReservasDetailEvent()
    object OnShowDeleteConfirmationDialog : ReservasDetailEvent()
    object OnHideDeleteConfirmationDialog : ReservasDetailEvent()
    object OnDelete : ReservasDetailEvent()
}