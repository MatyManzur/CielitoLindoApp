package com.example.cielitolindo.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

enum class Categoria(val stringName: String): Element {
    LIMPIEZA("Limpieza"),
    INSUMOS("Insumos"),
    LAVADERO("Lavadero"),
    PILETA("Pileta/Pasto"),
    LUZ("Luz"),
    MUNICIPAL("Municipal"),
    ARBA("Arba"),
    GAS("Gas"),
    DIRECTV("DirecTV"),
    INTERNET("Internet/Alarma"),
    OTRO("Otros");
    override fun toString(): String {
        return stringName
    }

    @Composable
    fun getIcon(): ImageVector {
        return when(this) {
            LIMPIEZA -> Icons.Filled.CleaningServices
            INSUMOS -> Icons.Filled.Sanitizer
            LAVADERO -> Icons.Filled.DryCleaning
            PILETA -> Icons.Filled.Yard
            LUZ -> Icons.Filled.Lightbulb
            MUNICIPAL -> Icons.Filled.RequestPage
            ARBA -> Icons.Filled.RequestPage
            GAS -> Icons.Filled.GasMeter
            DIRECTV -> Icons.Filled.LiveTv
            INTERNET -> Icons.Filled.Router
            OTRO -> Icons.Filled.OtherHouses
        }
    }
}