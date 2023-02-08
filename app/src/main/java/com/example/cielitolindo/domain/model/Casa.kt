package com.example.cielitolindo.domain.model

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.cielitolindo.ui.theme.onTertiary
import com.example.cielitolindo.ui.theme.tertiary
import com.example.cielitolindo.ui.theme.tertiaryVariant

enum class Casa(val stringName: String): Element{
    CELESTE("Celeste"),
    NARANJA("Naranja"),
    VERDE("Verde");

    override fun toString(): String {
        return stringName
    }

    @Composable
    fun getFirstColor() : Color {
        return when (this) {
            CELESTE -> MaterialTheme.colors.primary
            NARANJA -> MaterialTheme.colors.secondary
            VERDE -> MaterialTheme.colors.tertiary
        }
    }

    @Composable
    fun getSecondColor() : Color {
        return when (this) {
            CELESTE -> MaterialTheme.colors.primaryVariant
            NARANJA -> MaterialTheme.colors.secondaryVariant
            VERDE -> MaterialTheme.colors.tertiaryVariant
        }
    }

    @Composable
    fun getOnColor() : Color {
        return when (this) {
            CELESTE -> MaterialTheme.colors.onPrimary
            NARANJA -> MaterialTheme.colors.onSecondary
            VERDE -> MaterialTheme.colors.onTertiary
        }
    }
}