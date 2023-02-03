package com.example.cielitolindo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
)

private val LightColorPalette = lightColors(
    primary = LightBlue700,
    primaryVariant = LightBlue700Light,
    secondary = Orange700,
    secondaryVariant = Orange800Light,
    onPrimary = Color.White,
    onSecondary = Color.White,
)

val Colors.tertiary: Color
    @Composable
    get() = if (isSystemInDarkTheme()) LightGreen600Dark else LightGreen600Dark

val Colors.tertiaryVariant: Color
    @Composable
    get() = if (isSystemInDarkTheme()) LightGreen600 else LightGreen600

val Colors.onTertiary: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color.White else Color.White

@Composable
fun CielitoLindoTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}