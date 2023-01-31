package com.example.cielitolindo.domain.util

sealed class ClienteOrder(val orderType: OrderType) {
    class Nombre(orderType: OrderType): ClienteOrder(orderType)
    class Apellido(orderType: OrderType): ClienteOrder(orderType)
    class FechaInscripcion(orderType: OrderType): ClienteOrder(orderType)

    fun copy(orderType: OrderType): ClienteOrder {
        return when (this) {
            is FechaInscripcion -> FechaInscripcion(orderType)
            is Nombre -> Nombre(orderType)
            is Apellido -> Apellido(orderType)
        }
    }
}
