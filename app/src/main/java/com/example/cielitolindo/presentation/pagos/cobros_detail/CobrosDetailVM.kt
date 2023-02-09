package com.example.cielitolindo.presentation.pagos.cobros_detail

import android.os.Handler
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cielitolindo.domain.use_case.clientes.ClienteUseCases
import com.example.cielitolindo.domain.use_case.cobros.CobroUseCases
import com.example.cielitolindo.domain.use_case.reservas.ReservaUseCases
import com.example.cielitolindo.presentation.util.LoadingInfo
import com.example.cielitolindo.presentation.util.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CobrosDetailVM @Inject constructor(
    private val reservaUseCases: ReservaUseCases,
    private val clienteUseCases: ClienteUseCases,
    private val cobroUseCases: CobroUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = mutableStateOf(CobrosDetailState())
    val state: State<CobrosDetailState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<String>("cobroId")?.let { id ->
            viewModelScope.launch {
                cobroUseCases.getCobro(id)?.also { cobro ->
                    _state.value = state.value.copy(
                        id = id,
                        reserva = reservaUseCases.getReserva(cobro.reservaId),
                        cliente = clienteUseCases.getCliente(cobro.clienteId),
                        cobro = cobro
                    )
                }
            }
        }
    }

    suspend fun updateCobro() {
        cobroUseCases.getCobro(state.value.id)?.also { cobro ->
            _state.value = state.value.copy(
                reserva = reservaUseCases.getReserva(cobro.reservaId),
                cliente = clienteUseCases.getCliente(cobro.clienteId),
                cobro = cobro
            )
        }
    }

    fun onEvent(event: CobrosDetailEvent) {
        when (event) {
            CobrosDetailEvent.OnDelete -> {
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
                                            "Ocurrió un error en la conexión a la base de datos remota, el cobro se eliminará localmente y se intentará de nuevo cuando mejore la conexión!"
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
                    val cobro = state.value.cobro
                    try {
                        if (cobro != null) {
                            cobroUseCases.deleteCobro(
                                cobro = cobro,
                                onFirebaseSuccessListener = {
                                    _state.value = state.value.copy(
                                        loadingInfo = LoadingInfo(
                                            LoadingState.SUCCESS,
                                            "El cobro ha sido eliminado correctamente!"
                                        )
                                    )
                                },
                                onFirebaseFailureListener = {
                                    _state.value = state.value.copy(
                                        loadingInfo = LoadingInfo(
                                            LoadingState.ERROR,
                                            "Error al eliminar el cobro: ${it.message}"
                                        )
                                    )
                                }
                            )
                        } else {
                            _state.value = state.value.copy(
                                loadingInfo = LoadingInfo(
                                    LoadingState.ERROR,
                                    "Error inesperado: No se pudo encontrar el cobro!"
                                )
                            )
                        }

                    } catch (e: Exception) {
                        _state.value = state.value.copy(
                            loadingInfo = LoadingInfo(
                                LoadingState.ERROR,
                                "Error al eliminar el cobro: ${e.message}"
                            )
                        )
                    }
                }
            }
            CobrosDetailEvent.OnEdit -> {
                if (state.value.id != "") {
                    viewModelScope.launch {
                        _eventFlow.emit(UiEvent.EditCobro(state.value.id))
                    }
                } else {
                    viewModelScope.launch {
                        _eventFlow.emit(UiEvent.ShowSnackbar("Error inesperado: No se encontró el cobro"))
                    }
                }
            }
            CobrosDetailEvent.OnHideDeleteConfirmationDialog -> {
                _state.value = state.value.copy(
                    showDeleteConfirmationDialog = false
                )
            }
            CobrosDetailEvent.OnShowDeleteConfirmationDialog -> {
                _state.value = state.value.copy(
                    showDeleteConfirmationDialog = true
                )
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        data class EditCobro(val cobroId: String) : UiEvent()
        object Exit : UiEvent()
    }
}