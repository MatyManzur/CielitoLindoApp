package com.example.cielitolindo.presentation.pagos.gastos_add_edit

import android.os.Handler
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cielitolindo.data.data_source.formatter
import com.example.cielitolindo.domain.model.Categoria
import com.example.cielitolindo.domain.model.Gasto
import com.example.cielitolindo.domain.use_case.gastos.GastoUseCases
import com.example.cielitolindo.presentation.util.LoadingInfo
import com.example.cielitolindo.presentation.util.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class GastosAddEditVM @Inject constructor(
    private val gastoUseCases: GastoUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _state = mutableStateOf(GastosAddEditState())
    val state: State<GastosAddEditState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    val localformatter = DateTimeFormatter.ofPattern("dd/MM/yy")

    init {
        savedStateHandle.get<String>("gastoId")?.let { id ->
            viewModelScope.launch {
                gastoUseCases.getGasto(id)?.also { gasto ->
                    _state.value = state.value.copy(
                        screenTitle = "Editar Gasto",
                        id = gasto.id,
                        fecha = gasto.fecha,
                        descripcion = state.value.descripcion.copy(text = gasto.descripcion ?: ""),
                        categoria = gasto.categoria,
                        importe = state.value.importe.copy(text = gasto.importe.toString()),
                        moneda = gasto.moneda
                    )
                }
            }
        }
        savedStateHandle.get<String>("fecha")?.let { fecha ->
            _state.value = state.value.copy(fecha = LocalDate.parse(fecha, formatter))
        }
    }

    fun onEvent(event: GastosAddEditEvent) {
        when (event) {
            is GastosAddEditEvent.EnteredCategoria -> {
                _state.value = state.value.copy(categoria = event.categoria)
            }
            is GastosAddEditEvent.EnteredDescripcion -> {
                _state.value = state.value.copy(descripcion = state.value.descripcion.copy(text = event.value))
            }
            is GastosAddEditEvent.EnteredFecha -> {
                _state.value = state.value.copy(fecha = event.date)
            }
            is GastosAddEditEvent.EnteredImporte -> {
                _state.value = state.value.copy(importe = state.value.importe.copy(text = event.value))
            }
            is GastosAddEditEvent.EnteredMoneda -> {
                _state.value = state.value.copy(moneda = event.moneda)
            }
            GastosAddEditEvent.OnDiscard -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.DiscardGasto)
                }
            }
            GastosAddEditEvent.OnSave -> {
                _state.value = state.value.copy(
                    loadingInfo = LoadingInfo(LoadingState.LOADING)
                )
                viewModelScope.launch {
                    if (!state.value.isSaveEnabled)
                        return@launch
                    val gasto = Gasto(
                        id = state.value.id,
                        fecha = state.value.fecha,
                        descripcion = state.value.descripcion.text,
                        importe = state.value.importe.text.replace(',', '.').toFloat(),
                        moneda = state.value.moneda,
                        categoria = state.value.categoria ?: Categoria.OTRO
                    )
                    Handler().postDelayed({
                        when (state.value.loadingInfo.loadingState) {
                            LoadingState.READY -> {

                            }
                            LoadingState.LOADING -> {
                                viewModelScope.launch {
                                    _eventFlow.emit(
                                        UiEvent.ShowSnackbar(
                                            "Ocurrió un error en la conexión a la base de datos remota, el gasto se guardará localmente y se intentará de nuevo cuando mejore la conexión!"
                                        )
                                    )
                                    _eventFlow.emit(UiEvent.SaveGasto)
                                }
                            }
                            LoadingState.SUCCESS -> {
                                viewModelScope.launch {
                                    _eventFlow.emit(UiEvent.SaveGasto)
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
                        gastoUseCases.addGasto(
                            gasto,
                            onFirebaseSuccessListener = {
                                _state.value = state.value.copy(
                                    loadingInfo = LoadingInfo(
                                        LoadingState.SUCCESS,
                                        "El gasto ha sido guardado correctamente!"
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
                                        "Error al guardar el gasto: ${it.message}"
                                    )
                                )
                            }
                        )
                    } catch (e: Exception) {
                        _state.value = state.value.copy(
                            loadingInfo = LoadingInfo(
                                LoadingState.ERROR,
                                "Error al guardar el gasto: ${e.message}"
                            )
                        )
                    }
                }
            }
            is GastosAddEditEvent.ExpandDropdownMenu -> {
                if(event.value == null) {
                    _state.value = state.value.copy(
                        dropdownMenuExpanded = !state.value.dropdownMenuExpanded
                    )
                }
                else {
                    _state.value = state.value.copy(
                        dropdownMenuExpanded = event.value
                    )
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveGasto : UiEvent()
        object DiscardGasto : UiEvent()
    }
}