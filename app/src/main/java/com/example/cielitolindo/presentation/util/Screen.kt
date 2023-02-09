package com.example.cielitolindo.presentation.util

sealed class Screen(val route: String) {
    object ClientesMainScreen : Screen("clientes_main_screen")
    object ClientesAddEditScreen : Screen("clientes_add_edit_screen")
    object ClientesDetailScreen : Screen("clientes_detail_screen")
    object ReservasMainScreen : Screen("reservas_main_screen")
    object ReservasAddEditScreen : Screen("reservas_add_edit_screen")
    object ReservasDetailScreen : Screen("reservas_detail_screen")
    object PagosMainScreen: Screen("pagos_main_screen")
    object CobrosAddEditScreen: Screen("cobros_add_edit_screen")
    object CobrosDetailScreen: Screen("cobros_detail_screen")
    object GastosAddEditScreen: Screen("gastos_add_edit_screen")
    object GastosDetailScreen: Screen("gastos_detail_screen")
}
