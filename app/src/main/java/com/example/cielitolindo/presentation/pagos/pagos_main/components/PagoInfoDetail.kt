package com.example.cielitolindo.presentation.pagos.pagos_main.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.cielitolindo.domain.model.Moneda
import com.example.cielitolindo.presentation.pagos.pagos_main.util.PagoInfo
import java.time.format.DateTimeFormatter

@Composable
fun PagoInfoDetail(
    pagoInfo: PagoInfo,
    prependIcon: ImageVector? = null,
    prependIconColor: Color? = null,
    onSeeDetailClick: () -> Unit = {},
    onSeeDetailButtonColor: Color? = null,
    importesColor: Color,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(20f)) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (prependIcon != null && prependIconColor != null) {
                    Surface(
                        color = prependIconColor,
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Icon(
                            imageVector = prependIcon,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
                Column {
                    Text(text = pagoInfo.descripcion, style = MaterialTheme.typography.body1)
                    if (pagoInfo.fecha != null) {
                        Text(
                            text = pagoInfo.fecha.format(DateTimeFormatter.ofPattern("dd/MM/yy")),
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.onSurface.copy(alpha = .6f)
                        )
                    }
                }
            }
        }
        var first = true
        for (moneda in Moneda.values()) {
            if (first && pagoInfo.importes.contains(moneda)) {
                first = false
            } else {
                Column(
                    modifier = Modifier.weight(2f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (pagoInfo.importes.contains(moneda))
                        Text(
                            text = "+",
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.onSurface.copy(alpha = .6f)
                        )
                }
            }
            Column(
                modifier = Modifier.weight(10f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (pagoInfo.importes.contains(moneda)) {
                    Text(
                        text = moneda.importeToString(pagoInfo.importes[moneda] ?: 0f),
                        style = MaterialTheme.typography.body1,
                        color = importesColor
                    )
                }
            }
        }
        Column(modifier = Modifier.weight(5f), horizontalAlignment = Alignment.End) {
            if (onSeeDetailButtonColor != null) {
                IconButton(onClick = onSeeDetailClick) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Ver Detalle",
                        tint = onSeeDetailButtonColor
                    )
                }
            }
        }
    }
}