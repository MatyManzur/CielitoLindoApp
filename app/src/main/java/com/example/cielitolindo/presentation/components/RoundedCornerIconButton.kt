package com.example.cielitolindo.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.cielitolindo.presentation.reservas.reservas_add_edit.ReservasAddEditEvent
import com.google.android.gms.common.SignInButton.ButtonSize

@Composable
fun RoundedCornerIconButton(
    onClick: () -> Unit,
    color: Color = MaterialTheme.colors.secondary,
    onColor: Color = MaterialTheme.colors.onSecondary,
    icon: ImageVector,
    contentDescription: String,
    buttonSize: Dp = 55.dp,
    iconSize: Dp = 32.dp
) {
    IconButton(
        onClick = {onClick()},
        modifier = Modifier
            .width(buttonSize).height(buttonSize)
            .background(
                color = color,
                shape = RoundedCornerShape(6.dp)
            )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = onColor,
            modifier = Modifier.size(iconSize)
        )
    }
}