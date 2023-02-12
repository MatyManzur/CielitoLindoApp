package com.example.cielitolindo.presentation.pagos.cobros_add_edit

import android.os.Handler
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cielitolindo.domain.model.Cobro
import com.example.cielitolindo.domain.use_case.clientes.ClienteUseCases
import com.example.cielitolindo.domain.use_case.cobros.CobroUseCases
import com.example.cielitolindo.domain.use_case.reservas.ReservaUseCases
import com.example.cielitolindo.presentation.util.LoadingInfo
import com.example.cielitolindo.presentation.util.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class CobrosAddEditVM @Inject constructor(
    private val cobroUseCases: CobroUseCases,
    private val reservaUseCases: ReservaUseCases,
    private val clienteUseCases: ClienteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = mutableStateOf(CobrosAddEditState())
    val state: State<CobrosAddEditState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    val formatter = DateTimeFormatter.ofPattern("dd/MM/yy")

    init {
        savedStateHandle.get<String>("cobroId")?.let { id ->
            if(id.isNotBlank()) {
                viewModelScope.launch {
                    cobroUseCases.getCobro(id)?.also { cobro ->
                        _state.value = state.value.copy(
                            screenTitle = "Editar Cobro",
                            id = cobro.id,
                            cliente = clienteUseCases.getCliente(cobro.clienteId),
                            reserva = reservaUseCases.getReserva(cobro.reservaId),
                            fechaPago = cobro.fechaPago,
                            modoPago = state.value.modoPago.copy(text = cobro.modoPago ?: ""),
                            descripcion = state.value.descripcion.copy(text = cobro.descripcion ?: ""),
                            importe = state.value.importe.copy(text = cobro.importe.toString()),
                            moneda = cobro.moneda,
                            enConceptoDe = state.value.enConceptoDe.copy(text = cobro.enConceptoDe?.toString() ?: "")
                        )
                    }
                }
            }
        }
        savedStateHandle.get<String>("reservaId")?.let { id ->
            if(id.isNotBlank()) {
                viewModelScope.launch {
                    reservaUseCases.getReserva(id)?.also { reserva ->
                        _state.value = CobrosAddEditState(
                            reserva = reserva,
                            cliente = clienteUseCases.getCliente(reserva.clienteId)
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: CobrosAddEditEvent) {
        when (event) {
            is CobrosAddEditEvent.EnteredDescripcion -> {
                _state.value = state.value.copy(
                    descripcion = state.value.descripcion.copy(text = event.value)
                )
            }
            is CobrosAddEditEvent.EnteredFechaPago -> {
                _state.value = state.value.copy(
                    fechaPago = event.date
                )
            }
            is CobrosAddEditEvent.EnteredImporte -> {
                _state.value = state.value.copy(
                    importe = state.value.importe.copy(text = event.value)
                )
            }
            is CobrosAddEditEvent.EnteredEnConceptoDe -> {
                _state.value = state.value.copy(
                    enConceptoDe = state.value.enConceptoDe.copy(text = event.value)
                )
            }
            is CobrosAddEditEvent.EnteredModoPago -> {
                _state.value = state.value.copy(
                    modoPago = state.value.modoPago.copy(text = event.value)
                )
            }
            is CobrosAddEditEvent.EnteredMoneda -> {
                _state.value = state.value.copy(
                    moneda = event.moneda,
                )
            }
            CobrosAddEditEvent.OnDiscard -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.DiscardCobro)
                }
            }
            CobrosAddEditEvent.OnSave -> {
                _state.value = state.value.copy(
                    loadingInfo = LoadingInfo(LoadingState.LOADING)
                )
                viewModelScope.launch {
                    if (!state.value.isSaveEnabled)
                        return@launch
                    val cobro = Cobro(
                        id = state.value.id,
                        clienteId = state.value.cliente?.id ?: "",
                        reservaId = state.value.reserva?.id ?: "",
                        fechaPago = state.value.fechaPago,
                        modoPago = state.value.modoPago.text,
                        descripcion = state.value.descripcion.text,
                        importe = state.value.importe.text.replace(',', '.').toFloat(),
                        enConceptoDe = if(state.value.moneda !== state.value.reserva?.moneda) state.value.enConceptoDe.text.replace(',', '.').toFloat() else null,
                        moneda = state.value.moneda!!
                    )
                    Handler().postDelayed({
                        when (state.value.loadingInfo.loadingState) {
                            LoadingState.READY -> {

                            }
                            LoadingState.LOADING -> {
                                viewModelScope.launch {
                                    _eventFlow.emit(
                                        UiEvent.ShowSnackbar(
                                            "Ocurrió un error en la conexión a la base de datos remota, el cobro se guardará localmente y se intentará de nuevo cuando mejore la conexión!"
                                        )
                                    )
                                    _eventFlow.emit(UiEvent.SaveCobro)
                                }
                            }
                            LoadingState.SUCCESS -> {
                                viewModelScope.launch {
                                    _eventFlow.emit(UiEvent.SaveCobro)
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
                        cobroUseCases.addCobro(
                            cobro,
                            onFirebaseSuccessListener = {
                                _state.value = state.value.copy(
                                    loadingInfo = LoadingInfo(
                                        LoadingState.SUCCESS,
                                        "El cobro ha sido guardado correctamente!"
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
                                        "Error al guardar el cobro: ${it.message}"
                                    )
                                )
                            }
                        )
                    } catch (e: Exception) {
                        _state.value = state.value.copy(
                            loadingInfo = LoadingInfo(
                                LoadingState.ERROR,
                                "Error al guardar el cobro: ${e.message}"
                            )
                        )
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveCobro : UiEvent()
        object DiscardCobro : UiEvent()
    }
}