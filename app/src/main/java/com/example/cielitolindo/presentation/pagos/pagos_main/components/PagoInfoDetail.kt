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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.cielitolindo.domain.model.Moneda
import com.example.cielitolindo.presentation.pagos.pagos_main.util.PagoInfo
import java.time.format.DateTimeFormatter

@Composable
fun PagoInfoDetail(
    pagoInfo: PagoInfo,
    prependIcon: ImageVector? = null,
    prependIconColor: Color? = null,
    onPostpendIconClick: () -> Unit = {},
    postpendIconColor: Color? = null,
    postpendIcon: ImageVector = Icons.Filled.Search,
    importesColor: Color,
    dividerAtEnd: Boolean = false,
    connectingImportes: String = "+",
    noImportes: Boolean = false,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(20f),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
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
                        Text(
                            text = pagoInfo.descripcion,
                            style = MaterialTheme.typography.body2,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = if(noImportes) FontWeight.Medium else FontWeight.Normal
                        )
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
            if(!noImportes) {
                var first = true
                for (moneda in Moneda.values()) {
                    if (first && pagoInfo.importes.contains(moneda)) {
                        first = false
                    } else {
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            if (pagoInfo.importes.contains(moneda))
                                Text(
                                    text = connectingImportes,
                                    style = MaterialTheme.typography.caption,
                                    color = MaterialTheme.colors.onSurface.copy(alpha = .6f)
                                )
                        }
                    }
                    Column(
                        modifier = Modifier.weight(18f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (pagoInfo.importes.contains(moneda)) {
                            Text(
                                text = moneda.importeToString(pagoInfo.importes[moneda] ?: 0f),
                                style = MaterialTheme.typography.body2,
                                color = importesColor
                            )
                        }
                    }
                }
            }
            Column(
                modifier = Modifier.weight(5f), horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                if (postpendIconColor != null) {
                    IconButton(onClick = onPostpendIconClick) {
                        Icon(
                            imageVector = postpendIcon,
                            contentDescription = "Ver Detalle",
                            tint = postpendIconColor
                        )
                    }
                }
            }
        }
        if(dividerAtEnd)
            Divider(modifier = Modifier.fillMaxWidth())
    }

}