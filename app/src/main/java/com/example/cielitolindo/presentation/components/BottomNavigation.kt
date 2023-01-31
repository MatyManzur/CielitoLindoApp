package com.example.cielitolindo.presentation.components

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.People
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomNavigationOptions(val title: String, val icon: ImageVector) {
    CLIENTES("Clientes", Icons.Filled.People),
    RESERVAS("Reservas", Icons.Filled.EventAvailable),
    PAGOS("Pagos", Icons.Filled.Payments)
}

@Composable
fun BottomNav(currentRoute: BottomNavigationOptions, onNavigateToClientes: () -> Unit, onNavigateToReservas: () -> Unit, onNavigateToPagos: () -> Unit) {

    Surface {
        BottomNavigation(backgroundColor = MaterialTheme.colors.primary) {
            BottomNavigationItem(
                selected = currentRoute == BottomNavigationOptions.CLIENTES,
                onClick = {
                    if(currentRoute != BottomNavigationOptions.CLIENTES)
                        onNavigateToClientes()
                },
                label = { Text(text = BottomNavigationOptions.CLIENTES.title) },
                icon = { Icon(imageVector = BottomNavigationOptions.CLIENTES.icon, contentDescription = BottomNavigationOptions.CLIENTES.title) },
            )
            BottomNavigationItem(
                selected = currentRoute == BottomNavigationOptions.RESERVAS,
                onClick = {
                    if(currentRoute != BottomNavigationOptions.RESERVAS)
                        onNavigateToReservas()
                },
                label = { Text(text = BottomNavigationOptions.RESERVAS.title) },
                icon = { Icon(imageVector = BottomNavigationOptions.RESERVAS.icon, contentDescription = BottomNavigationOptions.RESERVAS.title) },
            )
            BottomNavigationItem(
                selected = currentRoute == BottomNavigationOptions.PAGOS,
                onClick = {
                    if(currentRoute != BottomNavigationOptions.PAGOS)
                        onNavigateToPagos()
                },
                label = { Text(text = BottomNavigationOptions.PAGOS.title) },
                icon = { Icon(imageVector = BottomNavigationOptions.PAGOS.icon, contentDescription = BottomNavigationOptions.PAGOS.title) },
            )
        }
    }

}