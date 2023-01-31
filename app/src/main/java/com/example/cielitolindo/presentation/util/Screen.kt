package com.example.cielitolindo.presentation.util

sealed class Screen(val route: String) {
    object ClientesMainScreen : Screen("clientes_main_screen")
    object ClientesAddEditScreen : Screen("clientes_add_edit_screen")
    object ClientesDetailScreen : Screen("clientes_detail_screen")
}
