package com.example.cielitolindo.presentation.reservas.reservas_main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import java.time.LocalDate
import java.time.Month

@Composable
fun DaysNumberLabels(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.onSurface,
    todayColor: Color = MaterialTheme.colors.secondary,
    startingDay: LocalDate,
    month: Month,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        var currentDay = startingDay
        for (i in 1..7) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                val colorForNumber = if(currentDay == LocalDate.now()) todayColor else color
                Text(
                    text = currentDay.dayOfMonth.toString(),
                    style = MaterialTheme.typography.body1,
                    fontWeight = if(currentDay == LocalDate.now()) FontWeight.ExtraBold else FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    color = colorForNumber.copy(alpha = if(currentDay.month == month) 1f else .3f)
                )
                currentDay = currentDay.plusDays(1)
            }
        }
    }
}