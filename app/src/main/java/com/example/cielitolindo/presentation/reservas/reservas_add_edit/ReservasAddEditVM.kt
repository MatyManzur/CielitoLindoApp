package com.example.cielitolindo.presentation.reservas.reservas_add_edit

import android.os.Handler
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cielitolindo.domain.model.Casa
import com.example.cielitolindo.domain.model.Reserva
import com.example.cielitolindo.domain.model.getNombreCompleto
import com.example.cielitolindo.domain.use_case.clientes.ClienteUseCases
import com.example.cielitolindo.domain.use_case.reservas.ReservaUseCases
import com.example.cielitolindo.presentation.util.LoadingInfo
import com.example.cielitolindo.presentation.util.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ReservasAddEditVM @Inject constructor(
    private val reservaUseCases: ReservaUseCases,
    private val clienteUseCases: ClienteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = mutableStateOf(ReservasAddEditState())
    val state: State<ReservasAddEditState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    val formatter = DateTimeFormatter.ofPattern("dd/MM/yy")

    init {
        savedStateHandle.get<String>("reservaId")?.let { id ->
            viewModelScope.launch {
                reservaUseCases.getReserva(id)?.also { reserva ->
                    _state.value = state.value.copy(
                        screenTitle = "Editar Reserva",
                        id = reserva.id,
                        cliente = state.value.cliente.copy(
                            text = clienteUseCases.getCliente(reserva.clienteId)
                                ?.getNombreCompleto() ?: ""
                        ),
                        clienteId = reserva.clienteId,
                        casa = reserva.casa,
                        fechaIngreso = reserva.fechaIngreso,
                        fechaEgreso = reserva.fechaEgreso,
                        importeTotal = state.value.importeTotal.copy(text = reserva.importeTotal.toString()),
                        moneda = reserva.moneda,
                        observaciones = state.value.observaciones.copy(
                            text = reserva.observaciones ?: ""
                        ),
                    )
                }
            }
        }
        savedStateHandle.get<String>("casa")?.let { casa ->
            _state.value = state.value.copy(casa = Casa.valueOf(casa))
        }
        viewModelScope.launch {
            clienteUseCases.getClientes().let {
                _state.value = state.value.copy(
                    allClientes = it
                )
            }
        }

    }

    fun onEvent(event: ReservasAddEditEvent) {
        when (event) {
            is ReservasAddEditEvent.EnteredCasa -> {
                _state.value = state.value.copy(casa = event.value)
            }
            is ReservasAddEditEvent.EnteredCliente -> {
                _state.value = state.value.copy(
                    cliente = state.value.cliente.copy(text = event.nombre),
                    clienteId = event.id
                )
            }
            is ReservasAddEditEvent.EnteredFechaEgreso -> {
                _state.value = state.value.copy(fechaEgreso = event.date)
            }
            is ReservasAddEditEvent.EnteredFechaIngreso -> {
                _state.value = state.value.copy(fechaIngreso = event.date)
            }
            is ReservasAddEditEvent.EnteredImporteTotal -> {
                _state.value =
                    state.value.copy(importeTotal = state.value.importeTotal.copy(text = event.value))
            }
            is ReservasAddEditEvent.EnteredMoneda -> {
                _state.value = state.value.copy(moneda = event.value)
            }
            is ReservasAddEditEvent.EnteredObservaciones -> {
                _state.value =
                    state.value.copy(observaciones = state.value.observaciones.copy(text = event.value))
            }
            ReservasAddEditEvent.OnDiscard -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.DiscardReserva)
                }
            }
            ReservasAddEditEvent.OnSave -> {
                _state.value = state.value.copy(
                    loadingInfo = LoadingInfo(LoadingState.LOADING)
                )
                viewModelScope.launch {
                    if (!state.value.isSaveEnabled)
                        return@launch
                    val reserva = Reserva(
                        id = state.value.id,
                        clienteId = state.value.clienteId,
                        casa = state.value.casa!!,
                        fechaIngreso = state.value.fechaIngreso!!,
                        fechaEgreso = state.value.fechaEgreso!!,
                        moneda = state.value.moneda!!,
                        observaciones = state.value.observaciones.text,
                        importeTotal = state.value.importeTotal.text.replace(',', '.').toFloat()
                    )
                    Handler().postDelayed({
                        when (state.value.loadingInfo.loadingState) {
                            LoadingState.READY -> {

                            }
                            LoadingState.LOADING -> {
                                viewModelScope.launch {
                                    _eventFlow.emit(
                                        UiEvent.ShowSnackbar(
                                            "Ocurrió un error en la conexión a la base de datos remota, la reserva se guardará localmente y se intentará de nuevo cuando mejore la conexión!"
                                        )
                                    )
                                    _eventFlow.emit(UiEvent.SaveReserva)
                                }
                            }
                            LoadingState.SUCCESS -> {
                                viewModelScope.launch {
                                    _eventFlow.emit(UiEvent.SaveReserva)
                                }
                            }
                            LoadingState.ERROR -> {
                                viewModelScope.launch {
                                    _eventFlow.emit(
                                        UiEvent.ShowSnackbar(state.value.loadingInfo.message)
                                    )
                                }
                            }
                        }
                        _state.value = state.value.copy(
                            loadingInfo = LoadingInfo(LoadingState.READY)
                        )
                    }, state.value.loadingInfo.loadingTimeout)
                    try {
                        reservaUseCases.addReserva(
                            reserva,
                            onFirebaseSuccessListener = {
                                _state.value = state.value.copy(
                                    loadingInfo = LoadingInfo(
                                        LoadingState.SUCCESS,
                                        "La reserva ha sido guardada correctamente!"
                                    )
                                )
                                viewModelScope.launch {
                                    _eventFlow.emit(
                                        UiEvent.ShowSnackbar(state.value.loadingInfo.message)
                                    )
                                }
                            },
                            onFirebaseFailureListener = {
                                _state.value = state.value.copy(
                                    loadingInfo = LoadingInfo(
                                        LoadingState.ERROR,
                                        "Error al guardar la reserva: ${it.message}"
                                    )
                                )
                            }
                        )
                    } catch (e: Exception) {
                        _state.value = state.value.copy(
                            loadingInfo = LoadingInfo(
                                LoadingState.ERROR,
                                "Error al guardar reserva: ${e.message}"
                            )
                        )
                    }
                }
            }
            ReservasAddEditEvent.HideClienteSearchDialog -> {
                _state.value = state.value.copy(
                    showClienteSearchDialog = false
                )
            }
            ReservasAddEditEvent.ShowClienteSearchDialog -> {
                _state.value = state.value.copy(
                    showClienteSearchDialog = true
                )
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveReserva : UiEvent()
        object DiscardReserva : UiEvent()
    }
}