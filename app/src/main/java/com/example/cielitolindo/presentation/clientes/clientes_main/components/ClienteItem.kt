package com.example.cielitolindo.presentation.clientes.clientes_main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cielitolindo.domain.model.*
import com.example.cielitolindo.presentation.clientes.clientes_detail.components.ReservaButton
import com.example.cielitolindo.ui.theme.tertiary
import java.time.LocalDate

@Composable
fun ClienteItem(
    cliente: Cliente,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 8.dp,
    reservasOfCliente: List<Reserva>,
    saldoPendiente: Map<Moneda, Float>,
    onNavigateToReservaDetail: (String) -> Unit,
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
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Column(
                    modifier = Modifier
                        .weight(50f)
                        .padding(vertical = 8.dp, horizontal = 10.dp),
                ) {
                    Text(
                        text = "Últimos alquileres:",
                        style = MaterialTheme.typography.body2
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    for (reserva in reservasOfCliente.sortedBy { r -> r.fechaEgreso }.takeLast(2)) {
                        ReservaButton(
                            reserva = reserva,
                            onClick = { onNavigateToReservaDetail(reserva.id) },
                            modifier = Modifier
                                .padding(bottom = 4.dp)
                                .height(22.dp),
                            datePattern = "dd/MM/yy",
                        )
                    }
                }
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Divider(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(1.dp),
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f),
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(50f)
                        .padding(vertical = 8.dp, horizontal = 10.dp),
                ) {
                    Text(
                        text = "Saldo Pendiente:",
                        style = MaterialTheme.typography.body2
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    var first = true
                    for (saldo in saldoPendiente) {
                        Text(
                            text = (if(first) "" else "+ ") + saldo.key.importeToString(saldo.value),
                            style = MaterialTheme.typography.body1,
                            fontWeight = FontWeight.Medium,
                            color = if (saldo.value > 0.01f) MaterialTheme.colors.error else MaterialTheme.colors.tertiary,
                        )
                        first = false
                    }
                }
            }
        }
    }
}
