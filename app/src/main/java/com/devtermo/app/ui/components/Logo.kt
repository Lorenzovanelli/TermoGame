package com.devtermo.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.devtermo.app.logic.LetterState

private val letrasDoLogo = listOf(
    'D' to LetterState.CORRECT,
    'E' to LetterState.CORRECT,
    'V' to LetterState.CORRECT,
    'T' to LetterState.PRESENT,
    'E' to LetterState.ABSENT,
    'R' to LetterState.PRESENT,
    'M' to LetterState.CORRECT,
    'O' to LetterState.ABSENT
)

@Composable
fun DevTermoLogo(modifier: Modifier = Modifier, tamanho: Dp = 32.dp) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        letrasDoLogo.forEach { (letra, estado) ->
            LetterTile(letra = letra, estado = estado, tamanho = tamanho)
        }
    }
}