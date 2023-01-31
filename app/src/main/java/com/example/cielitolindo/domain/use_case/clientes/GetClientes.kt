package com.example.cielitolindo.domain.use_case.clientes

import com.example.cielitolindo.domain.model.Cliente
import com.example.cielitolindo.domain.repository.ClienteRepository
import com.example.cielitolindo.domain.util.ClienteOrder
import com.example.cielitolindo.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.format.DateTimeFormatter

class GetClientes(
    private val clienteRepository: ClienteRepository
) {
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    operator fun invoke(
        clienteOrder: ClienteOrder = ClienteOrder.FechaInscripcion(OrderType.Descending)
    ): Flow<List<Cliente>> {
        return clienteRepository.getClientes().map { clientes ->
            when(clienteOrder.orderType) {
                is OrderType.Ascending -> {
                    when(clienteOrder) {
                        is ClienteOrder.Nombre -> clientes.sortedBy { it.nombre }
                        is ClienteOrder.Apellido -> clientes.sortedBy { it.apellido }
                        is ClienteOrder.FechaInscripcion -> clientes.sortedBy { it.fechaInscripcion.format(formatter) }
                    }
                }
                is OrderType.Descending -> {
                    when(clienteOrder) {
                        is ClienteOrder.Nombre -> clientes.sortedByDescending { it.nombre }
                        is ClienteOrder.Apellido -> clientes.sortedByDescending { it.apellido }
                        is ClienteOrder.FechaInscripcion -> clientes.sortedByDescending { it.fechaInscripcion.format(formatter) }
                    }
                }
            }
        }
    }
}