package com.example.cielitolindo.presentation.clientes.clientes_main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.cielitolindo.domain.model.Cliente
import com.example.cielitolindo.domain.model.getDireccionCompleta
import com.example.cielitolindo.domain.model.getNombreCompleto
import java.time.LocalDate

@Composable
fun ClienteItem(
    cliente: Cliente,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 8.dp
) {
    Surface(
        modifier = modifier
            .padding(16.dp)
            .background(
                color = MaterialTheme.colors.surface,
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f),
                shape = RoundedCornerShape(cornerRadius)
            ),
        elevation = 8.dp,
        shape = RoundedCornerShape(cornerRadius),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = cliente.getNombreCompleto(),
                    style = MaterialTheme.typography.h6
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = cliente.getDireccionCompleta(),
                    style = MaterialTheme.typography.body1
                )
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth(),
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f),
            )
            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(0.5f).padding(vertical = 8.dp, horizontal = 16.dp),
                ) {
                    Text(
                        text = "Últimos alquileres:",
                        style = MaterialTheme.typography.body2
                    )
                }
                Divider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp),
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f),
                )
                Column(
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                ) {
                    Text(
                        text = "Saldo Pendiente:",
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        }
    }
}

//Preview
@Preview
@Composable
fun ClienteItemPreview() {
    val cliente = Cliente(
        id = "aaaakljfkdfja",
        nombre = "Juan",
        dni = 12345678,
        apellido = "Perez",
        direccion = "Calle 123",
        telefono = "123456789",
        email = "",
        localidad = "Villa Carlos Paz",
        provincia = "Cordoba",
        fechaInscripcion = LocalDate.now(),
        observaciones = ""
    )
    ClienteItem(cliente = cliente)
}
