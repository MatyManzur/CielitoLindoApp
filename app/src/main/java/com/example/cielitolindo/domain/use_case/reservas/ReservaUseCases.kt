package com.example.cielitolindo.domain.use_case.reservas

class ReservaUseCases(
    val addReserva: AddReserva,
    val deleteReserva: DeleteReserva,
    val fetchReservas: FetchReservas,
    val getAllReservas: GetAllReservas,
    val getReserva: GetReserva,
    val getReservasFromCliente: GetReservasFromCliente,
    val getReservasInRange: GetReservasInRange,
    val countReservasOfCasaInRange: CountReservasOfCasaInRange
)