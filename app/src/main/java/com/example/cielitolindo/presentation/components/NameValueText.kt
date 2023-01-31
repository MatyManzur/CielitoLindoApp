package com.example.cielitolindo.presentation.components

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun NameValueText(
    fieldName: String,
    fieldValue: String,
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colors.onSurface
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = fieldName,
            style = MaterialTheme.typography.caption,
            color = textColor.copy(alpha = 0.6f),
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
        Text(
            text = fieldValue,
            style = MaterialTheme.typography.body1,
            color = textColor,
            textAlign = TextAlign.Start,
        )
    }
}