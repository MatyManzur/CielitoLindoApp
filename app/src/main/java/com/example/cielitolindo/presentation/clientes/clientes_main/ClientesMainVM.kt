package com.example.cielitolindo.presentation.clientes.clientes_main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cielitolindo.domain.model.Moneda
import com.example.cielitolindo.domain.model.Reserva
import com.example.cielitolindo.domain.use_case.clientes.ClienteUseCases
import com.example.cielitolindo.domain.use_case.cobros.CobroUseCases
import com.example.cielitolindo.domain.use_case.reservas.ReservaUseCases
import com.example.cielitolindo.domain.util.ClienteOrder
import com.example.cielitolindo.domain.util.OrderType
import com.example.cielitolindo.presentation.util.LoadingInfo
import com.example.cielitolindo.presentation.util.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientesMainVM @Inject constructor(
    private val clienteUseCases: ClienteUseCases,
    private val reservaUseCases: ReservaUseCases,
    private val cobroUseCases: CobroUseCases
) : ViewModel() {
    private val _state = mutableStateOf(ClientesState())
    val state: State<ClientesState> = _state

    private var getClientesJob: Job? = null

    init {
        viewModelScope.launch {
            clienteUseCases.fetchClientes()
            getClientes(ClienteOrder.FechaInscripcion(OrderType.Descending))
        }
    }

    suspend fun updateClientes() {
        clienteUseCases.fetchClientes()
        getClientes(ClienteOrder.FechaInscripcion(OrderType.Descending))
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
                val map = mutableMapOf<String, List<Reserva>>()
                val saldosMap = mutableMapOf<String, Map<Moneda, Float>>()
                for (cliente in clientes) {
                    map[cliente.id] = reservaUseCases.getReservasFromCliente(cliente.id).first()
                    val saldoPendiente = mutableMapOf<Moneda, Float>()
                    if(map[cliente.id] != null) {
                        for (reserva in map[cliente.id]!!) {
                            saldoPendiente[reserva.moneda] = saldoPendiente[reserva.moneda]?.plus(reserva.importeTotal) ?: reserva.importeTotal
                            val cobros = cobroUseCases.getCobrosFromReserva(reserva.id).first()
                            for (cobro in cobros) {
                                saldoPendiente[reserva.moneda] = saldoPendiente[reserva.moneda]?.minus(cobro.enConceptoDe ?: cobro.importe) ?: cobro.enConceptoDe ?: cobro.importe
                            }
                        }
                    }
                    saldosMap[cliente.id] = saldoPendiente
                }
                _state.value = state.value.copy(
                    reservasOfClientes = map,
                    loadingInfo = LoadingInfo(LoadingState.READY),
                    saldosPendientesOfClientes = saldosMap
                )
            }
            .launchIn(viewModelScope)
    }
}