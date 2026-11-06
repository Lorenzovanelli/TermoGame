package com.devtermo.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/** Responsavel: Integrante 2 (identidade visual) com apoio do Integrante 1. */
object DevTermoColors {
    val Correto = Color(0xFF538D4E)
    val Presente = Color(0xFFB59F3B)
    val Ausente = Color(0xFF3A3A3C)
}

@Composable
fun DevTermoTheme(content: @Composable () -> Unit) {
    val scheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
    MaterialTheme(colorScheme = scheme, content = content)
}
