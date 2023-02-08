package com.example.cielitolindo.presentation.pagos.pagos_main.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SectionHeader(title: String, hideShowState: Boolean?, onShow: () -> Unit, onHide: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Divider(modifier = Modifier.fillMaxWidth())
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, style = MaterialTheme.typography.h5, fontWeight = FontWeight.Bold)
            if(hideShowState != null) {
                IconButton(onClick = if(hideShowState) onHide else onShow) {
                    Icon(
                        imageVector = if(hideShowState) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = "mostrar/ocultar detalle"
                    )
                }
            }
            else {
                Spacer(modifier = Modifier.width(24.dp))
            }
        }
        Divider(modifier = Modifier.fillMaxWidth())
    }
}