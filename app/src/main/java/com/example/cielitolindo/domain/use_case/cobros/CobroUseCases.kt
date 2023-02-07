package com.example.cielitolindo.domain.use_case.cobros

class CobroUseCases (
    val addCobro: AddCobro,
    val deleteCobro: DeleteCobro,
    val fetchCobros: FetchCobros,
    val getCobro: GetCobro,
    val getCobrosByPaymentDate: GetCobrosByPaymentDate,
    val getCobrosByReservaDate: GetCobrosByReservaDate,
    val getCobrosFromReserva: GetCobrosFromReserva,
    val getCobrosFromCliente: GetCobrosFromCliente,
)