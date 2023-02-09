package com.example.cielitolindo.presentation.clientes.clientes_main

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cielitolindo.domain.model.getNombreCompleto
import com.example.cielitolindo.presentation.clientes.clientes_main.components.ClienteItem
import com.example.cielitolindo.presentation.clientes.clientes_main.components.ClienteOrderSection
import com.example.cielitolindo.presentation.clientes.clientes_main.components.ExpandableSearchView
import com.example.cielitolindo.presentation.components.BottomNav
import com.example.cielitolindo.presentation.components.BottomNavigationOptions
import com.example.cielitolindo.presentation.components.LoadingDependingContent
import com.example.cielitolindo.presentation.components.Refreshable
import com.example.cielitolindo.presentation.util.ScaffoldElementsState

@Composable
fun ClientesMainScreen(
    onComposing: (ScaffoldElementsState) -> Unit,
    onShowSnackbar: (
        String,
        String?,
        SnackbarDuration
    ) -> Unit,
    onNavigateToCreateCliente: () -> Unit,
    onNavigateToClienteDetail: (String) -> Unit,
    onNavigateToReservaDetail: (String) -> Unit,
    onNavigateToReservas: () -> Unit,
    onNavigateToPagos: () -> Unit,
    viewModel: ClientesMainVM = hiltViewModel()
) {
    val state = viewModel.state.value
    val scope = rememberCoroutineScope()

    var search: String by remember {
        mutableStateOf("")
    }

    var isSearching: Boolean by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = true) {
        onComposing(
            ScaffoldElementsState(
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { onNavigateToCreateCliente() },
                        backgroundColor = MaterialTheme.colors.secondary
                    ) {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "Add",
                            tint = MaterialTheme.colors.onSecondary
                        )
                    }
                },
                topBar = {
                    Surface(
                        color = MaterialTheme.colors.primary,
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            ExpandableSearchView(
                                searchDisplay = search,
                                onSearchDisplayChanged = {
                                    search = it
                                },
                                onSearchDisplayClosed = {
                                    isSearching = false
                                },
                                onSearchDisplayOpened = {
                                    isSearching = true
                                },
                                onSortClick = {
                                    viewModel.onEvent(ClientesEvent.ToggleOrderSection)
                                }
                            )
                        }
                    }
                },
                bottomBar = {
                    BottomNav(
                        currentRoute = BottomNavigationOptions.CLIENTES,
                        onNavigateToClientes = {},
                        onNavigateToReservas = { onNavigateToReservas() },
                        onNavigateToPagos = { onNavigateToPagos() }
                    )
                }
            )
        )
    }

    Refreshable(refreshFunction = viewModel::updateClientes) {
        LoadingDependingContent(loadingInfo = state.loadingInfo) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                AnimatedVisibility(
                    visible = state.isOrderSectionVisible,
                    enter = fadeIn().plus(slideInVertically()),
                    exit = fadeOut().plus(slideOutVertically())
                ) {
                    ClienteOrderSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        clienteOrder = state.clientesOrder,
                        onOrderChange = { order ->
                            viewModel.onEvent(ClientesEvent.Order(order))
                        }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    state.clientes.forEach { cliente ->
                        if (!isSearching || cliente.getNombreCompleto().contains(search, true)) {
                            ClienteItem(
                                cliente = cliente,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onNavigateToClienteDetail(cliente.id)
                                    },
                                reservasOfCliente = state.reservasOfClientes.getOrDefault(
                                    cliente.id,
                                    listOf()
                                ),
                                onNavigateToReservaDetail = onNavigateToReservaDetail,
                                saldoPendiente = state.saldosPendientesOfClientes.getOrDefault(cliente.id, mapOf())
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }


}