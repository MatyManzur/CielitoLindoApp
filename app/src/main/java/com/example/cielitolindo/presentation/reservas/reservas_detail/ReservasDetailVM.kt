package com.example.cielitolindo.presentation.reservas.reservas_detail

import android.os.Handler
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cielitolindo.domain.model.Reserva
import com.example.cielitolindo.domain.model.getNombreCompleto
import com.example.cielitolindo.domain.use_case.clientes.ClienteUseCases
import com.example.cielitolindo.domain.use_case.reservas.ReservaUseCases
import com.example.cielitolindo.presentation.clientes.clientes_detail.ClientesDetailVM
import com.example.cielitolindo.presentation.util.LoadingInfo
import com.example.cielitolindo.presentation.util.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReservasDetailVM @Inject constructor(
    private val reservaUseCases: ReservaUseCases,
    private val clienteUseCases: ClienteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = mutableStateOf(ReservasDetailState())
    val state: State<ReservasDetailState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<String>("reservaId")?.let { id ->
            viewModelScope.launch {
                reservaUseCases.getReserva(id)?.also { reserva ->
                    _state.value = state.value.copy(
                        id = id,
                        reserva = reserva,
                        clienteName = clienteUseCases.getCliente(reserva.clienteId)?.getNombreCompleto() ?: ""
                    )
                }
            }
        }
    }

    suspend fun updateReserva() {
        reservaUseCases.getReserva(state.value.id)?.also { reserva ->
            _state.value = state.value.copy(
                reserva = reserva,
                clienteName = clienteUseCases.getCliente(reserva.clienteId)?.getNombreCompleto() ?: ""
            )
        }
    }

    fun onEvent(event: ReservasDetailEvent) {
        when (event) {
            ReservasDetailEvent.OnDelete -> {
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
                                            "Ocurrió un error en la conexión a la base de datos remota, la reserva se eliminará localmente y se intentará de nuevo cuando mejore la conexión!"
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
                    val reserva = state.value.reserva
                    try {
                        if (reserva != null) {
                            reservaUseCases.deleteReserva(
                                reserva = reserva,
                                onFirebaseSuccessListener = {
                                    _state.value = state.value.copy(
                                        loadingInfo = LoadingInfo(
                                            LoadingState.SUCCESS,
                                            "La reserva ha sido eliminada correctamente!"
                                        )
                                    )
                                },
                                onFirebaseFailureListener = {
                                    _state.value = state.value.copy(
                                        loadingInfo = LoadingInfo(
                                            LoadingState.ERROR,
                                            "Error al eliminar la reserva: ${it.message}"
                                        )
                                    )
                                }
                            )
                        } else {
                            _state.value = state.value.copy(
                                loadingInfo = LoadingInfo(
                                    LoadingState.ERROR,
                                    "Error inesperado: No se pudo encontrar la reserva!"
                                )
                            )
                        }

                    } catch (e: Exception) {
                        _state.value = state.value.copy(
                            loadingInfo = LoadingInfo(
                                LoadingState.ERROR,
                                "Error al eliminar la reserva: ${e.message}"
                            )
                        )
                    }
                }
            }
            ReservasDetailEvent.OnEdit -> {
                if (state.value.id != "") {
                    viewModelScope.launch {
                        _eventFlow.emit(UiEvent.EditReserva(state.value.id))
                    }
                } else {
                    viewModelScope.launch {
                        _eventFlow.emit(UiEvent.ShowSnackbar("Error inesperado: No se encontró la reserva"))
                    }
                }
            }
            ReservasDetailEvent.OnHideDeleteConfirmationDialog -> {
                _state.value = state.value.copy(
                    showDeleteConfirmationDialog = false
                )
            }
            ReservasDetailEvent.OnShowDeleteConfirmationDialog -> {
                _state.value = state.value.copy(
                    showDeleteConfirmationDialog = true
                )
            }
        }
    }


    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        data class EditReserva(val reservaId: String) : UiEvent()
        object Exit : UiEvent()
    }
}