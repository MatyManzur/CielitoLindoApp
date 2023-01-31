package com.example.cielitolindo.presentation.clientes.clientes_add_edit

import android.os.Handler
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cielitolindo.domain.model.Cliente
import com.example.cielitolindo.domain.model.getNombreCompleto
import com.example.cielitolindo.domain.use_case.clientes.ClienteUseCases
import com.example.cielitolindo.presentation.util.LoadingInfo
import com.example.cielitolindo.presentation.util.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientesAddEditVM @Inject constructor(
    private val clienteUseCases: ClienteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = mutableStateOf(ClientesAddEditState())
    val state: State<ClientesAddEditState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<String>("clienteId")?.let { id ->
            viewModelScope.launch {
                clienteUseCases.getCliente(id)?.also { cliente ->
                    _state.value = state.value.copy(
                        screenTitle = "Editar Cliente",
                        id = cliente.id,
                        nombre = state.value.nombre.copy(text = cliente.nombre),
                        apellido = state.value.apellido.copy(text = cliente.apellido ?: ""),
                        dni = state.value.dni.copy(text = cliente.dni.toString()),
                        direccion = state.value.direccion.copy(text = cliente.direccion ?: ""),
                        localidad = state.value.localidad.copy(text = cliente.localidad ?: ""),
                        provincia = state.value.provincia.copy(text = cliente.provincia ?: ""),
                        telefono = state.value.telefono.copy(text = cliente.telefono ?: ""),
                        email = state.value.email.copy(text = cliente.email ?: ""),
                        observaciones = state.value.observaciones.copy(
                            text = cliente.observaciones ?: ""
                        )
                    )
                }
            }
        }
    }

    fun onEvent(event: ClientesAddEditEvent) {
        when (event) {
            is ClientesAddEditEvent.EnteredNombre -> {
                _state.value = state.value.copy(
                    nombre = state.value.nombre.copy(
                        text = event.value
                    )
                )
            }
            is ClientesAddEditEvent.EnteredApellido -> {
                _state.value = state.value.copy(
                    apellido = state.value.apellido.copy(
                        text = event.value
                    )
                )
            }
            is ClientesAddEditEvent.EnteredDni -> {
                _state.value = state.value.copy(
                    dni = state.value.dni.copy(
                        text = event.value
                    )
                )
            }
            is ClientesAddEditEvent.EnteredDireccion -> {
                _state.value = state.value.copy(
                    direccion = state.value.direccion.copy(
                        text = event.value
                    )
                )
            }
            is ClientesAddEditEvent.EnteredLocalidad -> {
                _state.value = state.value.copy(
                    localidad = state.value.localidad.copy(
                        text = event.value
                    )
                )
            }
            is ClientesAddEditEvent.EnteredProvincia -> {
                _state.value = state.value.copy(
                    provincia = state.value.provincia.copy(
                        text = event.value
                    )
                )
            }
            is ClientesAddEditEvent.EnteredTelefono -> {
                _state.value = state.value.copy(
                    telefono = state.value.telefono.copy(
                        text = event.value
                    )
                )
            }
            is ClientesAddEditEvent.EnteredEmail -> {
                _state.value = state.value.copy(
                    email = state.value.email.copy(
                        text = event.value
                    )
                )
            }
            is ClientesAddEditEvent.EnteredObservaciones -> {
                _state.value = state.value.copy(
                    observaciones = state.value.observaciones.copy(
                        text = event.value
                    )
                )
            }
            is ClientesAddEditEvent.OnSave -> {
                _state.value = state.value.copy(
                    loadingInfo = LoadingInfo(LoadingState.LOADING)
                )
                viewModelScope.launch {
                    val cliente = Cliente(
                        id = state.value.id,
                        nombre = state.value.nombre.text,
                        apellido = state.value.apellido.text,
                        fechaInscripcion = state.value.fechaInscripcion,
                        dni = try {
                            state.value.dni.text.toInt()
                        } catch (e: NumberFormatException) {
                            null
                        },
                        direccion = state.value.direccion.text,
                        localidad = state.value.localidad.text,
                        provincia = state.value.provincia.text,
                        telefono = state.value.telefono.text,
                        email = state.value.email.text,
                        observaciones = state.value.observaciones.text
                    )
                    Handler().postDelayed({
                        when(state.value.loadingInfo.loadingState) {
                            LoadingState.READY -> {

                            }
                            LoadingState.LOADING -> {
                                viewModelScope.launch {
                                    _eventFlow.emit(
                                        UiEvent.ShowSnackbar(
                                            "Ocurrió un error en la conexión a la base de datos remota, el cliente se guardará localmente y se intentará de nuevo cuando mejore la conexión!"
                                        )
                                    )
                                    _eventFlow.emit(UiEvent.SaveCliente)
                                }
                            }
                            LoadingState.SUCCESS -> {
                                viewModelScope.launch {
                                    _eventFlow.emit(UiEvent.SaveCliente)
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
                        clienteUseCases.addCliente(cliente,
                            onFirebaseSuccessListener = {
                                _state.value = state.value.copy(
                                    loadingInfo = LoadingInfo(LoadingState.SUCCESS, "El cliente ${cliente.getNombreCompleto()} ha sido guardado correctamente!")
                                )
                                viewModelScope.launch {
                                    _eventFlow.emit(
                                        UiEvent.ShowSnackbar(state.value.loadingInfo.message)
                                    )
                                }
                            },
                            onFirebaseFailureListener = {
                                _state.value = state.value.copy(
                                    loadingInfo = LoadingInfo(LoadingState.ERROR, "Error al guardar el cliente ${cliente.getNombreCompleto()}: ${it.message}")
                                )
                            })
                    } catch (e: Exception) {
                        _state.value = state.value.copy(
                            loadingInfo = LoadingInfo(LoadingState.ERROR, "Error al guardar el cliente ${cliente.getNombreCompleto()}: ${e.message}")
                        )
                    }
                }
            }
            is ClientesAddEditEvent.OnDiscard -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.DiscardCliente)
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveCliente : UiEvent()
        object DiscardCliente : UiEvent()
    }
}