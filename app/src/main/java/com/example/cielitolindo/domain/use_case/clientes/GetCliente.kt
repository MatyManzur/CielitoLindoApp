package com.example.cielitolindo.domain.use_case.clientes

import com.example.cielitolindo.domain.model.Cliente
import com.example.cielitolindo.domain.repository.ClienteRepository

class GetCliente(
    private val clienteRepository: ClienteRepository
) {
    suspend operator fun invoke(id: String): Cliente? = clienteRepository.getClienteById(id)
}
