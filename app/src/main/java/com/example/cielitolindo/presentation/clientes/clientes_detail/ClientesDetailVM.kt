package com.example.cielitolindo.presentation.clientes.clientes_detail

import android.os.Handler
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cielitolindo.domain.model.getNombreCompleto
import com.example.cielitolindo.domain.use_case.clientes.ClienteUseCases
import com.example.cielitolindo.domain.use_case.reservas.ReservaUseCases
import com.example.cielitolindo.presentation.clientes.clientes_add_edit.ClientesAddEditVM
import com.example.cielitolindo.presentation.util.LoadingInfo
import com.example.cielitolindo.presentation.util.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientesDetailVM @Inject constructor(
    private val clienteUseCases: ClienteUseCases,
    private val reservaUseCases: ReservaUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = mutableStateOf(ClientesDetailState())
    val state: State<ClientesDetailState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<String>("clienteId")?.let { id ->
            viewModelScope.launch {
                clienteUseCases.getCliente(id)?.also { cliente ->
                    _state.value = state.value.copy(
                        id = id,
                        cliente = cliente
                    )
                }
                _state.value = state.value.copy(
                    reservasOfCliente = reservaUseCases.getReservasFromCliente(id).first()
                )
            }
        }
    }

    suspend fun updateCliente() {
        clienteUseCases.getCliente(state.value.id)?.also { cliente ->
            _state.value = state.value.copy(
                cliente = cliente
            )
        }
        _state.value = state.value.copy(
            reservasOfCliente = reservaUseCases.getReservasFromCliente(state.value.id).first()
        )
    }

    fun onEvent(event: ClientesDetailEvent) {
        when (event) {
            ClientesDetailEvent.OnDelete -> {
                _state.value = state.value.copy(
                    loadingInfo = LoadingInfo(LoadingState.LOADING)
                )
                viewModelScope.launch {
                    Handler().postDelayed({
                        when (state.value.loadingInfo.loadingState) {
                            LoadingState.READY -> {}
                            LoadingState.LOADING -> {
                                viewModelScope.launch {
                                    _eventFlow.emit(
                                        UiEvent.ShowSnackbar(
                                            "Ocurrió un error en la conexión a la base de datos remota, el cliente se eliminará localmente y se intentará de nuevo cuando mejore la conexión!"
                                        )
                                    )
                                    _eventFlow.emit(UiEvent.Exit)
                                }
                            }
                            LoadingState.SUCCESS -> {
                                viewModelScope.launch {
                                    _eventFlow.emit(
                                        UiEvent.ShowSnackbar(state.value.loadingInfo.message)
                                    )
                                    _eventFlow.emit(UiEvent.Exit)
                                }
                            }
                            LoadingState.ERROR -> {
                                _state.value = state.value.copy(
                                    showDeleteConfirmationDialog = false
                                )
                                viewModelScope.launch {
                                    _eventFlow.emit(
                                        UiEvent.ShowSnackbar(state.value.loadingInfo.message)
                                    )
                                }
                            }
                        }
                    }, state.value.loadingInfo.loadingTimeout)
                    val cliente = state.value.cliente
                    try {
                        if(cliente != null) {
                            clienteUseCases.deleteCliente(
                                cliente = cliente,
                                onFirebaseSuccessListener = {
                                    _state.value = state.value.copy(
                                        loadingInfo = LoadingInfo(LoadingState.SUCCESS, "El cliente ${cliente.getNombreCompleto()} ha sido eliminado correctamente!")
                                    )
                                },
                                onFirebaseFailureListener = {
                                    _state.value = state.value.copy(
                                        loadingInfo = LoadingInfo(LoadingState.ERROR, "Error al eliminar el cliente ${cliente.getNombreCompleto()}: ${it.message}")
                                    )
                                }
                            )
                        } else {
                            _state.value = state.value.copy(
                                loadingInfo = LoadingInfo(LoadingState.ERROR, "Error inesperado: No se pudo encontrar el cliente!")
                            )
                        }

                    } catch (e: Exception) {
                        _state.value = state.value.copy(
                            loadingInfo = LoadingInfo(LoadingState.ERROR, "Error al eliminar el cliente ${cliente?.getNombreCompleto() ?: ""}: ${e.message}")
                        )
                    }
                }
            }
            ClientesDetailEvent.OnEdit -> {
                if(state.value.id != "") {
                    viewModelScope.launch {
                        _eventFlow.emit(UiEvent.EditCliente(state.value.id))
                    }
                }
                else {
                    viewModelScope.launch {
                        _eventFlow.emit(UiEvent.ShowSnackbar("Error inesperado: No se encontró el cliente"))
                    }
                }
            }
            ClientesDetailEvent.OnShowDeleteConfirmationDialog -> {
                _state.value = state.value.copy(
                    showDeleteConfirmationDialog = true
                )
            }
            ClientesDetailEvent.OnHideDeleteConfirmationDialog -> {
                _state.value = state.value.copy(
                    showDeleteConfirmationDialog = false
                )
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        data class EditCliente(val clienteId: String) : UiEvent()
        object Exit : UiEvent()
    }

}

