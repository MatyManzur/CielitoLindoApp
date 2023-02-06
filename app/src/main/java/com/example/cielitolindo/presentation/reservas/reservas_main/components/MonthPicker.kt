package com.example.cielitolindo.presentation.reservas.reservas_main.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.cielitolindo.domain.model.Casa
import com.example.cielitolindo.domain.model.Reserva
import com.example.cielitolindo.presentation.util.MonthWeeks
import java.time.LocalDate
import java.time.Month
import java.time.Year
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Composable
fun MonthPicker(
    modifier: Modifier = Modifier,
    currentMonth: YearMonth,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    color: Color = MaterialTheme.colors.onSurface,
    buttonsEnabled: Boolean,
) {
    val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onPreviousMonth() }, enabled = buttonsEnabled) {
            Icon(
                imageVector = Icons.Filled.NavigateBefore,
                contentDescription = "Mes Anterior",
                modifier = Modifier.size(36.dp),
                tint = color
            )
        }
        Text(
            text = currentMonth.format(formatter).uppercase(),
            style = MaterialTheme.typography.h5,
            color = color,
            fontWeight = FontWeight.Bold
        )
        IconButton(onClick = { onNextMonth() }, enabled = buttonsEnabled) {
            Icon(
                imageVector = Icons.Filled.NavigateNext,
                contentDescription = "Mes Siguiente",
                modifier = Modifier.size(36.dp),
                tint = color
            )
        }
    }
}

