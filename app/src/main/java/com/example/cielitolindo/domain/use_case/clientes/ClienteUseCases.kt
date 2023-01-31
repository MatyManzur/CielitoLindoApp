package com.example.cielitolindo.domain.use_case.clientes

class ClienteUseCases(
    val getClientes: GetClientes,
    val deleteCliente: DeleteCliente,
    val addCliente: AddCliente,
    val fetchClientes: FetchClientes,
    val getCliente: GetCliente
)