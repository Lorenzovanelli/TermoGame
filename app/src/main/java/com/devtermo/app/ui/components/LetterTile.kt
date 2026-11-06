package com.devtermo.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devtermo.app.logic.LetterState
import com.devtermo.app.ui.theme.DevTermoColors

/** Responsavel: Integrante 2. Quadrado de uma letra. estado == null: ainda nao avaliada. */
@Composable
fun LetterTile(letra: Char?, estado: LetterState?, modifier: Modifier = Modifier) {
    val fundo = when (estado) {
        LetterState.CORRECT -> DevTermoColors.Correto
        LetterState.PRESENT -> DevTermoColors.Presente
        LetterState.ABSENT -> DevTermoColors.Ausente
        null -> Color.Transparent
    }
    val corTexto = if (estado == null) MaterialTheme.colorScheme.onSurface else Color.White
    val forma = RoundedCornerShape(4.dp)

    Box(
        modifier = modifier
            .size(44.dp)
            .clip(forma)
            .background(fundo)
            .border(2.dp, MaterialTheme.colorScheme.outline, forma),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letra?.toString().orEmpty(),
            color = corTexto,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
    }
}
