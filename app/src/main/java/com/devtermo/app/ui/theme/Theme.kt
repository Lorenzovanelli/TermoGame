package com.devtermo.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object DevTermoColors {
    val Correto = Color(0xFF538D4E)
    val Presente = Color(0xFFB59F3B)
    val Ausente = Color(0xFF3A3A3C)
}

private val EsquemaClaro = lightColorScheme(
    primary = DevTermoColors.Correto,
    onPrimary = Color.White,
    secondary = DevTermoColors.Presente,
    onSecondary = Color.White,
    background = Color(0xFFFAFAFA),
    onBackground = Color(0xFF1A1A1B),
    surface = Color(0xFFFAFAFA),
    onSurface = Color(0xFF1A1A1B),
    outline = Color(0xFFD3D6DA)
)

private val EsquemaEscuro = darkColorScheme(
    primary = DevTermoColors.Correto,
    onPrimary = Color.White,
    secondary = DevTermoColors.Presente,
    onSecondary = Color.White,
    background = Color(0xFF121213),
    onBackground = Color(0xFFFFFFFF),
    surface = Color(0xFF121213),
    onSurface = Color(0xFFFFFFFF),
    outline = Color(0xFF565758)
)

@Composable
fun DevTermoTheme(content: @Composable () -> Unit) {
    val scheme = if (isSystemInDarkTheme()) EsquemaEscuro else EsquemaClaro
    MaterialTheme(colorScheme = scheme, content = content)
}