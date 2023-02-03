package com.example.cielitolindo.presentation.reservas.reservas_main

sealed class ReservasEvent {
    object PreviousMonth : ReservasEvent()
    object NextMonth : ReservasEvent()
    object ToggleCasa : ReservasEvent()
    object FetchReservas : ReservasEvent()
}