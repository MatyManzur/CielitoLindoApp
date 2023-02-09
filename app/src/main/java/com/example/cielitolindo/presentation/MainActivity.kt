package com.example.cielitolindo.presentation

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cielitolindo.presentation.clientes.clientes_add_edit.ClientesAddEditScreen
import com.example.cielitolindo.presentation.clientes.clientes_detail.ClientesDetailScreen
import com.example.cielitolindo.presentation.clientes.clientes_main.ClientesMainScreen
import com.example.cielitolindo.presentation.pagos.cobros_add_edit.CobrosAddEditScreen
import com.example.cielitolindo.presentation.pagos.cobros_detail.CobrosDetailScreen
import com.example.cielitolindo.presentation.pagos.gastos_add_edit.GastosAddEditScreen
import com.example.cielitolindo.presentation.pagos.gastos_detail.GastosDetailScreen
import com.example.cielitolindo.presentation.pagos.pagos_main.PagosMainScreen
import com.example.cielitolindo.presentation.reservas.reservas_add_edit.ReservasAddEditScreen
import com.example.cielitolindo.presentation.reservas.reservas_detail.ReservasDetailScreen
import com.example.cielitolindo.presentation.reservas.reservas_main.ReservasMainScreen
import com.example.cielitolindo.presentation.util.ScaffoldElementsState
import com.example.cielitolindo.presentation.util.Screen
import com.example.cielitolindo.ui.theme.CielitoLindoTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CielitoLindoTheme {
                val scaffoldState = rememberScaffoldState()
                val navController = rememberNavController()
                val scope = rememberCoroutineScope()
                Surface(color = MaterialTheme.colors.background) {
                    var scaffoldElementsState by remember {
                        mutableStateOf(ScaffoldElementsState())
                    }
                    Scaffold(
                        topBar = scaffoldElementsState.topBar,
                        bottomBar = scaffoldElementsState.bottomBar,
                        floatingActionButton = scaffoldElementsState.floatingActionButton,
                        scaffoldState = scaffoldState
                    ) { paddingValues ->
                        NavHost(
                            navController = navController,
                            startDestination = Screen.ReservasMainScreen.route,
                            modifier = Modifier.padding(paddingValues)
                        ) {
                            composable(route = Screen.ClientesMainScreen.route) {
                                ClientesMainScreen(
                                    onComposing = {
                                        scaffoldElementsState = it
                                    },
                                    onShowSnackbar = { message, actionLabel, duration ->
                                        scope.launch {
                                            scaffoldState.snackbarHostState.showSnackbar(
                                                message,
                                                actionLabel,
                                                duration
                                            )
                                        }
                                    },
                                    onNavigateToCreateCliente = {
                                        navController.navigate(Screen.ClientesAddEditScreen.route)
                                    },
                                    onNavigateToClienteDetail = { clienteId ->
                                        navController.navigate(Screen.ClientesDetailScreen.route + "?clienteId=$clienteId")
                                    },
                                    onNavigateToReservaDetail = { reservaId ->
                                        navController.navigate(Screen.ReservasDetailScreen.route + "?reservaId=$reservaId")
                                    },
                                    onNavigateToReservas = {
                                        navController.navigate(Screen.ReservasMainScreen.route)
                                    },
                                    onNavigateToPagos = {
                                        navController.navigate(Screen.PagosMainScreen.route)
                                    },
                                )
                            }
                            composable(
                                route = Screen.ClientesAddEditScreen.route + "?clienteId={clienteId}",
                                arguments = listOf(
                                    navArgument(
                                        name = "clienteId"
                                    ) {
                                        type = NavType.StringType
                                        defaultValue = ""
                                    }
                                )
                            ) {
                                ClientesAddEditScreen(
                                    onComposing = {
                                        scaffoldElementsState = it
                                    },
                                    onShowSnackbar = { message, actionLabel, duration ->
                                        scope.launch {
                                            scaffoldState.snackbarHostState.showSnackbar(
                                                message,
                                                actionLabel,
                                                duration
                                            )
                                        }
                                    },
                                    onNavigateUp = {
                                        navController.popBackStack()
                                    },
                                )
                            }
                            composable(
                                route = Screen.ClientesDetailScreen.route + "?clienteId={clienteId}",
                                arguments = listOf(
                                    navArgument(
                                        name = "clienteId"
                                    ) {
                                        type = NavType.StringType
                                        defaultValue = ""
                                    }
                                )
                            ) {
                                ClientesDetailScreen(
                                    onComposing = {
                                        scaffoldElementsState = it
                                    },
                                    onShowSnackbar = { message, actionLabel, duration ->
                                        scope.launch {
                                            scaffoldState.snackbarHostState.showSnackbar(
                                                message,
                                                actionLabel,
                                                duration
                                            )
                                        }
                                    },
                                    onNavigateUp = {
                                        navController.navigate(Screen.ClientesMainScreen.route)
                                    },
                                    onNavigateToEditCliente = { clienteId ->
                                        navController.navigate(Screen.ClientesAddEditScreen.route + "?clienteId=$clienteId")
                                    },
                                    onNavigateToReservaDetail = { reservaId ->
                                        navController.navigate(Screen.ReservasDetailScreen.route + "?reservaId=$reservaId")
                                    }
                                )
                            }
                            composable(route = Screen.ReservasMainScreen.route) {
                                ReservasMainScreen(
                                    onComposing = {
                                        scaffoldElementsState = it
                                    },
                                    onShowSnackbar = { message, actionLabel, duration ->
                                        scope.launch {
                                            scaffoldState.snackbarHostState.showSnackbar(
                                                message,
                                                actionLabel,
                                                duration
                                            )
                                        }
                                    },
                                    onNavigateToCreateReserva = {
                                        navController.navigate(Screen.ReservasAddEditScreen.route)
                                    },
                                    onNavigateToReservaDetail = { reservaId ->
                                        navController.navigate(Screen.ReservasDetailScreen.route + "?reservaId=$reservaId")
                                    },
                                    onNavigateToClientes = {
                                        navController.navigate(Screen.ClientesMainScreen.route)
                                    },
                                    onNavigateToPagos = {
                                        navController.navigate(Screen.PagosMainScreen.route)
                                    },
                                )
                            }
                            composable(
                                route = Screen.ReservasAddEditScreen.route + "?reservaId={reservaId}",
                                arguments = listOf(
                                    navArgument(
                                        name = "reservaId"
                                    ) {
                                        type = NavType.StringType
                                        defaultValue = ""
                                    }
                                )
                            ) {
                                ReservasAddEditScreen(
                                    onComposing = {
                                        scaffoldElementsState = it
                                    },
                                    onShowSnackbar = { message, actionLabel, duration ->
                                        scope.launch {
                                            scaffoldState.snackbarHostState.showSnackbar(
                                                message,
                                                actionLabel,
                                                duration
                                            )
                                        }
                                    },
                                    onNavigateUp = {
                                        navController.popBackStack()
                                    },
                                    supportFragmentManager = supportFragmentManager
                                )
                            }
                            composable(
                                route = Screen.ReservasDetailScreen.route + "?reservaId={reservaId}",
                                arguments = listOf(
                                    navArgument(
                                        name = "reservaId"
                                    ) {
                                        type = NavType.StringType
                                        defaultValue = ""
                                    }
                                )
                            ) {
                                ReservasDetailScreen(
                                    onComposing = {
                                        scaffoldElementsState = it
                                    },
                                    onShowSnackbar = { message, actionLabel, duration ->
                                        scope.launch {
                                            scaffoldState.snackbarHostState.showSnackbar(
                                                message,
                                                actionLabel,
                                                duration
                                            )
                                        }
                                    },
                                    onNavigateUp = {
                                        navController.navigate(Screen.ReservasMainScreen.route)
                                    },
                                    onNavigateToEditReserva = { reservaId ->
                                        navController.navigate(Screen.ReservasAddEditScreen.route + "?reservaId=$reservaId")
                                    },
                                    onNavigateToClienteDetail = { clienteId ->
                                        navController.navigate(Screen.ClientesDetailScreen.route + "?clienteId=$clienteId")
                                    },
                                    onNavigateToCreateCobro = { reservaId ->
                                        navController.navigate(Screen.CobrosAddEditScreen.route + "?reservaId=$reservaId")
                                    },
                                    onNavigateToCobroDetail = { cobroId ->
                                        navController.navigate(Screen.CobrosDetailScreen.route + "?cobroId=$cobroId")
                                    }
                                )
                            }
                            composable(
                                route = Screen.PagosMainScreen.route
                            ) {
                                PagosMainScreen(
                                    onComposing = {
                                        scaffoldElementsState = it
                                    },
                                    onShowSnackbar = { message, actionLabel, duration ->
                                        scope.launch {
                                            scaffoldState.snackbarHostState.showSnackbar(
                                                message,
                                                actionLabel,
                                                duration
                                            )
                                        }
                                    },
                                    onNavigateToCreateGasto = {
                                        navController.navigate(Screen.GastosAddEditScreen.route)
                                    },
                                    onNavigateToCobroDetail = { cobroId ->
                                        navController.navigate(Screen.CobrosDetailScreen.route + "?cobroId=$cobroId")
                                    },
                                    onNavigateToGastoDetail = { gastoId ->
                                        navController.navigate(Screen.GastosDetailScreen.route + "?gastoId=$gastoId")
                                    },
                                    onNavigateToClientes = {
                                        navController.navigate(Screen.ClientesMainScreen.route)
                                    },
                                    onNavigateToReservas = {
                                        navController.navigate(Screen.ReservasMainScreen.route)
                                    },
                                    sharedPreferences = getSharedPreferences(
                                        "SETTINGS",
                                        Context.MODE_PRIVATE
                                    )
                                )
                            }
                            composable(
                                route = Screen.CobrosAddEditScreen.route + "?cobroId={cobroId}&reservaId={reservaId}",
                                arguments = listOf(
                                    navArgument(
                                        name = "cobroId"
                                    ) {
                                        type = NavType.StringType
                                        defaultValue = ""
                                    },
                                    navArgument(
                                        name = "reservaId"
                                    ) {
                                        type = NavType.StringType
                                        defaultValue = ""
                                    }
                                )
                            ) {
                                CobrosAddEditScreen(
                                    onComposing = {
                                        scaffoldElementsState = it
                                    },
                                    onShowSnackbar = { message, actionLabel, duration ->
                                        scope.launch {
                                            scaffoldState.snackbarHostState.showSnackbar(
                                                message,
                                                actionLabel,
                                                duration
                                            )
                                        }
                                    },
                                    onNavigateUp = {
                                        navController.popBackStack()
                                    },
                                )
                            }
                            composable(
                                route = Screen.GastosAddEditScreen.route + "?gastoId={gastoId}",
                                arguments = listOf(
                                    navArgument(
                                        name = "gastoId"
                                    ) {
                                        type = NavType.StringType
                                        defaultValue = ""
                                    }
                                )
                            ) {
                                GastosAddEditScreen(
                                    onComposing = {
                                        scaffoldElementsState = it
                                    },
                                    onShowSnackbar = { message, actionLabel, duration ->
                                        scope.launch {
                                            scaffoldState.snackbarHostState.showSnackbar(
                                                message,
                                                actionLabel,
                                                duration
                                            )
                                        }
                                    },
                                    onNavigateUp = {
                                        navController.popBackStack()
                                    },
                                )
                            }
                            composable(
                                route = Screen.CobrosDetailScreen.route + "?cobroId={cobroId}",
                                arguments = listOf(
                                    navArgument(
                                        name = "cobroId"
                                    ) {
                                        type = NavType.StringType
                                        defaultValue = ""
                                    }
                                )
                            ) {
                                CobrosDetailScreen(
                                    onComposing = {
                                        scaffoldElementsState = it
                                    },
                                    onShowSnackbar = { message, actionLabel, duration ->
                                        scope.launch {
                                            scaffoldState.snackbarHostState.showSnackbar(
                                                message,
                                                actionLabel,
                                                duration
                                            )
                                        }
                                    },
                                    onNavigateUp = {
                                        navController.navigate(Screen.PagosMainScreen.route)
                                    },
                                    onNavigateToEditCobro = { cobroId ->
                                        navController.navigate(Screen.CobrosAddEditScreen.route + "?cobroId=$cobroId")
                                    },
                                    onNavigateToReservaDetail = { reservaId ->
                                        navController.navigate(Screen.ReservasDetailScreen.route + "?reservaId=$reservaId")
                                    },
                                    onNavigateToClienteDetail = { clienteId ->
                                        navController.navigate(Screen.ClientesDetailScreen.route + "?clienteId=$clienteId")
                                    }
                                )
                            }
                            composable(
                                route = Screen.GastosDetailScreen.route + "?gastoId={gastoId}",
                                arguments = listOf(
                                    navArgument(
                                        name = "gastoId"
                                    ) {
                                        type = NavType.StringType
                                        defaultValue = ""
                                    }
                                )
                            ) {
                                GastosDetailScreen(
                                    onComposing = {
                                        scaffoldElementsState = it
                                    },
                                    onShowSnackbar = { message, actionLabel, duration ->
                                        scope.launch {
                                            scaffoldState.snackbarHostState.showSnackbar(
                                                message,
                                                actionLabel,
                                                duration
                                            )
                                        }
                                    },
                                    onNavigateUp = {
                                        navController.navigate(Screen.PagosMainScreen.route)
                                    },
                                    onNavigateToEditGasto = { gastoId ->
                                        navController.navigate(Screen.GastosAddEditScreen.route + "?gastoId=$gastoId")
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}