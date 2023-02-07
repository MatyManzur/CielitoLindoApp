package com.example.cielitolindo.domain.use_case.gastos

class GastoUseCases (
    val addGasto: AddGasto,
    val deleteGasto: DeleteGasto,
    val fetchGastos: FetchGastos,
    val getGasto: GetGasto,
    val getGastosInRange: GetGastosInRange,
)