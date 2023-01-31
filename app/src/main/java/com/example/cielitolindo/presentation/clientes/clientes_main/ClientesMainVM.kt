package com.example.cielitolindo.presentation.clientes.clientes_main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cielitolindo.domain.model.Cliente
import com.example.cielitolindo.domain.use_case.clientes.ClienteUseCases
import com.example.cielitolindo.domain.util.ClienteOrder
import com.example.cielitolindo.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientesMainVM @Inject constructor(
    private val clienteUseCases: ClienteUseCases
) : ViewModel() {
    private val _state = mutableStateOf(ClientesState())
    val state: State<ClientesState> = _state

    private var recentlyDeletedCliente: Cliente? = null

    private var getClientesJob: Job? = null

    init {
        viewModelScope.launch {
            clienteUseCases.fetchClientes()
            getClientes(ClienteOrder.FechaInscripcion(OrderType.Descending))
        }
    }

    fun onEvent(event: ClientesEvent) {
        when (event) {
            is ClientesEvent.Order -> {
                if (state.value.clientesOrder::class == event.clienteOrder::class &&
                    state.value.clientesOrder.orderType == event.clienteOrder.orderType
                ) {
                    return
                }
                getClientes(event.clienteOrder)
            }
            is ClientesEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
            is ClientesEvent.FetchClientes -> {
                viewModelScope.launch {
                    clienteUseCases.fetchClientes()
                    getClientes(ClienteOrder.FechaInscripcion(OrderType.Descending))
                }
            }
        }
    }

    private fun getClientes(clienteOrder: ClienteOrder) {
        getClientesJob?.cancel()
        getClientesJob = clienteUseCases.getClientes(clienteOrder)
            .onEach { clientes ->
                _state.value = state.value.copy(
                    clientes = clientes,
                    clientesOrder = clienteOrder
                )
            }
            .launchIn(viewModelScope)
        _state.value = state.value.copy(isLoading = false)
    }
}