package com.example.cielitolindo.presentation.reservas.reservas_main.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.cielitolindo.domain.model.Reserva
import com.example.cielitolindo.domain.model.getShortRangoDeFechas
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Composable
fun ReservasStripe(
    onClickReserva: (Reserva) -> Unit,
    getClienteNameAtIndex: (Int) -> String,
    modifier: Modifier = Modifier,
    firstColor: Color,
    secondColor: Color,
    onColor: Color,
    reservas: List<Reserva>,
    firstDayOfWeek: LocalDate,
    stripeSize: StripeSize = StripeSize.SINGLE_LINE,
) {
    var day = firstDayOfWeek
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(stripeSize.height),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        var firstOffset = 0.5f
        var j = 0
        for ((i, reserva) in reservas.withIndex()) {
            if(!day.isAfter(reserva.fechaIngreso)) {
                val spaceRange = ChronoUnit.DAYS.between(day, reserva.fechaIngreso)
                Spacer(modifier = Modifier.weight(spaceRange.toFloat() + firstOffset))
                firstOffset = 0f
                day = day.plusDays(spaceRange)
            }
            val range = ChronoUnit.DAYS.between(day, reserva.fechaEgreso)
            Button(
                onClick = { onClickReserva(reserva) },
                modifier = Modifier
                    .height(stripeSize.height)
                    .weight(range.toFloat() + firstOffset),
                colors = ButtonDefaults.buttonColors(backgroundColor = if(j++ % 2 == 0) firstColor else secondColor)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 2.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    when (stripeSize) {
                        StripeSize.SINGLE_LINE -> {
                            Text(
                                text = "${getClienteNameAtIndex(i)} ${reserva.getShortRangoDeFechas()}".uppercase(),
                                style = MaterialTheme.typography.body1,
                                fontWeight = FontWeight.Bold,
                                color = onColor,
                            )
                        }
                        StripeSize.EXPANDED -> {
                            Text(
                                text = getClienteNameAtIndex(i).uppercase(),
                                style = MaterialTheme.typography.h5,
                                fontWeight = FontWeight.Bold,
                                color = onColor,
                            )
                            Text(
                                text = reserva.getShortRangoDeFechas().uppercase(),
                                style = MaterialTheme.typography.h6,
                                fontWeight = FontWeight.Normal,
                                color = onColor,
                            )
                            Text(
                                text = reserva.moneda.importeToString(reserva.importeTotal),
                                style = MaterialTheme.typography.body2,
                                fontWeight = FontWeight.Light,
                                color = onColor,
                            )
                        }
                    }
                }
            }
            firstOffset = 0f
            day = day.plusDays(range)
        }
    }
}

enum class StripeSize(val height: Dp) {
    SINGLE_LINE(20.dp),
    EXPANDED(60.dp)
}