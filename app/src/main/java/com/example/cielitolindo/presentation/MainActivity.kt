package com.example.cielitolindo.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cielitolindo.presentation.clientes.clientes_add_edit.ClientesAddEditScreen
import com.example.cielitolindo.presentation.clientes.clientes_detail.ClientesDetailScreen
import com.example.cielitolindo.presentation.clientes.clientes_main.ClientesMainScreen
import com.example.cielitolindo.presentation.util.ScaffoldElementsState
import com.example.cielitolindo.presentation.util.Screen
import com.example.cielitolindo.ui.theme.CielitoLindoTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
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
                            startDestination = Screen.ClientesMainScreen.route,
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
                                    onNavigateToReservas = {
                                        //TODO
                                    },
                                    onNavigateToPagos = {
                                        //TODO
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
                                        navController.popBackStack()
                                    },
                                    onNavigateToEditCliente = { clienteId ->
                                        navController.navigate(Screen.ClientesAddEditScreen.route + "?clienteId=$clienteId")
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