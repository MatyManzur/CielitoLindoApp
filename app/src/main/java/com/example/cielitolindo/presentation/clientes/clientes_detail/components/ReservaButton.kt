package com.example.cielitolindo.presentation.clientes.clientes_detail.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.cielitolindo.domain.model.Reserva
import com.example.cielitolindo.domain.model.getColor
import com.example.cielitolindo.domain.model.getRangoDeFechasString

@Composable
fun ReservaButton(
    modifier: Modifier = Modifier,
    reserva: Reserva,
    onClick: (Reserva) -> Unit,
    textSize: TextUnit = 14.sp,
    datePattern: String = "dd MMM yyyy",
) {
    Box(
        modifier = modifier,
    ) {
        Button(
            onClick = { onClick(reserva) },
            modifier = Modifier.fillMaxSize(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = reserva.getColor()
            )
        ) {}
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = reserva.getRangoDeFechasString(datePattern, "->").uppercase(),
                fontSize = textSize,
                fontWeight = FontWeight.Bold,
                color = reserva.casa.getOnColor(),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}