package com.example.cielitolindo.presentation.pagos.gastos_detail

import android.os.Handler
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cielitolindo.domain.use_case.gastos.GastoUseCases
import com.example.cielitolindo.presentation.util.LoadingInfo
import com.example.cielitolindo.presentation.util.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GastosDetailVM @Inject constructor(
    private val gastoUseCases: GastoUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = mutableStateOf(GastosDetailState())
    val state: State<GastosDetailState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<String>("gastoId")?.let { id ->
            viewModelScope.launch {
                gastoUseCases.getGasto(id)?.also { gasto ->
                    _state.value = state.value.copy(
                        id = id,
                        gasto = gasto
                    )
                }
            }
        }
    }

    suspend fun updateGasto() {
        gastoUseCases.getGasto(state.value.id)?.also { gasto ->
            _state.value = state.value.copy(
                gasto = gasto
            )
        }
    }

    fun onEvent(event: GastosDetailEvent) {
        when (event) {
            GastosDetailEvent.OnDelete -> {
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
                                            "Ocurrió un error en la conexión a la base de datos remota, el gasto se eliminará localmente y se intentará de nuevo cuando mejore la conexión!"
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
                    val gasto = state.value.gasto
                    try {
                        if (gasto != null) {
                            gastoUseCases.deleteGasto(
                                gasto = gasto,
                                onFirebaseSuccessListener = {
                                    _state.value = state.value.copy(
                                        loadingInfo = LoadingInfo(
                                            LoadingState.SUCCESS,
                                            "El gasto ha sido eliminado correctamente!"
                                        )
                                    )
                                },
                                onFirebaseFailureListener = {
                                    _state.value = state.value.copy(
                                        loadingInfo = LoadingInfo(
                                            LoadingState.ERROR,
                                            "Error al eliminar el gasto: ${it.message}"
                                        )
                                    )
                                }
                            )
                        } else {
                            _state.value = state.value.copy(
                                loadingInfo = LoadingInfo(
                                    LoadingState.ERROR,
                                    "Error inesperado: No se pudo encontrar el gasto!"
                                )
                            )
                        }

                    } catch (e: Exception) {
                        _state.value = state.value.copy(
                            loadingInfo = LoadingInfo(
                                LoadingState.ERROR,
                                "Error al eliminar el gasto: ${e.message}"
                            )
                        )
                    }
                }
            }
            GastosDetailEvent.OnEdit -> {
                if (state.value.id != "") {
                    viewModelScope.launch {
                        _eventFlow.emit(UiEvent.EditGasto(state.value.id))
                    }
                } else {
                    viewModelScope.launch {
                        _eventFlow.emit(UiEvent.ShowSnackbar("Error inesperado: No se encontró el gasto"))
                    }
                }
            }
            GastosDetailEvent.OnHideDeleteConfirmationDialog -> {
                _state.value = state.value.copy(
                    showDeleteConfirmationDialog = false
                )
            }
            GastosDetailEvent.OnShowDeleteConfirmationDialog -> {
                _state.value = state.value.copy(
                    showDeleteConfirmationDialog = true
                )
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        data class EditGasto(val gastoId: String) : UiEvent()
        object Exit : UiEvent()
    }
}