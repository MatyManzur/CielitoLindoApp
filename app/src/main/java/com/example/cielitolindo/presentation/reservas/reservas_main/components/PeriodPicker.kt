package com.example.cielitolindo.presentation.reservas.reservas_main.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Composable
fun PeriodPicker(
    modifier: Modifier = Modifier,
    currentPeriod: String,
    onPreviousPeriod: () -> Unit,
    onNextPeriod: () -> Unit,
    color: Color = MaterialTheme.colors.onSurface,
    buttonsEnabled: Boolean,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onPreviousPeriod() }, enabled = buttonsEnabled) {
            Icon(
                imageVector = Icons.Filled.NavigateBefore,
                contentDescription = "Mes Anterior",
                modifier = Modifier.size(36.dp),
                tint = color
            )
        }
        Text(
            text = currentPeriod.uppercase(),
            style = MaterialTheme.typography.h5,
            color = color,
            fontWeight = FontWeight.Bold
        )
        IconButton(onClick = { onNextPeriod() }, enabled = buttonsEnabled) {
            Icon(
                imageVector = Icons.Filled.NavigateNext,
                contentDescription = "Mes Siguiente",
                modifier = Modifier.size(36.dp),
                tint = color
            )
        }
    }
}

