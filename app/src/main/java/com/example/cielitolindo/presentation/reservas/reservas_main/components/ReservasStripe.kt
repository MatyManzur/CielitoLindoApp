package com.example.cielitolindo.presentation.reservas.reservas_main.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cielitolindo.domain.model.Reserva
import com.example.cielitolindo.domain.model.getRangoDeFechasString
import com.example.cielitolindo.presentation.util.colorVariation
import java.time.LocalDate
import java.time.temporal.ChronoUnit



@Composable
fun ReservasStripe(
    onClickReserva: (Reserva) -> Unit,
    getClienteNameAtIndex: (Int) -> String,
    modifier: Modifier = Modifier,
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
        val lastDayOfWeek = firstDayOfWeek.plusDays(6)
        var offsetForLastSpace = 0.5f
        for ((i, reserva) in reservas.withIndex()) {
            if (day.isEqual(firstDayOfWeek) && !day.isAfter(reserva.fechaIngreso)) {
                Spacer(modifier = Modifier.weight(0.5f))
            }
            if (day.isBefore(reserva.fechaIngreso)) {
                val spaceRange = ChronoUnit.DAYS.between(day, reserva.fechaIngreso)
                Spacer(modifier = Modifier.weight(spaceRange.toFloat()))
                //Text(text="...${spaceRange.toFloat() + if(day==firstDayOfWeek) 0.5f else 0f}...")
                day = day.plusDays(spaceRange)
            }
            val range = ChronoUnit.DAYS.between(
                day,
                if (reserva.fechaEgreso.isBefore(lastDayOfWeek)) reserva.fechaEgreso else lastDayOfWeek
            )
            //Text(text="[${getClienteNameAtIndex(i)}: ${range.toFloat() + if(reserva.fechaIngreso.isBefore(firstDayOfWeek)) 0.5f else 0f + if(reserva.fechaEgreso.isAfter(lastDayOfWeek)) 0.5f else 0f}]")
            Box(
                modifier = Modifier
                    .height(stripeSize.height)
                    .weight(
                        range.toFloat() + if (reserva.fechaIngreso.isBefore(firstDayOfWeek)) 0.5f else 0f + if (reserva.fechaEgreso.isAfter(
                                lastDayOfWeek
                            )
                        ) 0.5f.also { offsetForLastSpace = 0f } else 0f.also {
                            offsetForLastSpace = 0.5f
                        }
                    ),
            ) {
                val baseColor = reserva.casa.getFirstColor()
                Button(
                    onClick = { onClickReserva(reserva) },
                    modifier = Modifier.fillMaxSize(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = baseColor.copy(
                            red = colorVariation(baseColor.red, reserva.id.hashCode(), 3),
                            green = colorVariation(baseColor.green, reserva.id.hashCode(), 9),
                            blue = colorVariation(baseColor.blue, reserva.id.hashCode(), 7)
                        )
                    )
                ) {}
                Row(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Spacer(modifier = Modifier.weight(2f))
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(96f),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        when (stripeSize) {
                            StripeSize.SINGLE_LINE -> {
                                Text(
                                    text = "\t${getClienteNameAtIndex(i)} ${reserva.getRangoDeFechasString()}".uppercase(),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = onColor,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1
                                )
                            }
                            StripeSize.EXPANDED -> {
                                Text(
                                    text = getClienteNameAtIndex(i).uppercase(),
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = onColor,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1
                                )
                                Row() {
                                    Text(
                                        text = reserva.getRangoDeFechasString().uppercase(),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = onColor,
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = " - " + reserva.moneda.importeToString(reserva.importeTotal),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Light,
                                        color = onColor,
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.weight(2f))
                }
            }
            day = day.plusDays(range)
        }
        if (day.isBefore(lastDayOfWeek) || (offsetForLastSpace > 0f && day.isEqual(lastDayOfWeek))) {
            val spaceRange = ChronoUnit.DAYS.between(day, lastDayOfWeek)
            Spacer(modifier = Modifier.weight(spaceRange.toFloat() + offsetForLastSpace))
        }
    }
}

enum class StripeSize(val height: Dp) {
    SINGLE_LINE(20.dp),
    EXPANDED(60.dp)
}