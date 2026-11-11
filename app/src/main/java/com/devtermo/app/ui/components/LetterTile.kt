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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devtermo.app.logic.LetterState
import com.devtermo.app.ui.theme.DevTermoColors

@Composable
fun LetterTile(
    letra: Char?,
    estado: LetterState?,
    modifier: Modifier = Modifier,
    tamanho: Dp = 44.dp
) {
    val fundo = when (estado) {
        LetterState.CORRECT -> DevTermoColors.Correto
        LetterState.PRESENT -> DevTermoColors.Presente
        LetterState.ABSENT -> DevTermoColors.Ausente
        null -> Color.Transparent
    }
    val corTexto = if (estado == null) MaterialTheme.colorScheme.onSurface else Color.White
    val corBorda = when {
        estado != null -> fundo
        letra != null -> MaterialTheme.colorScheme.onSurface
        else -> MaterialTheme.colorScheme.outline
    }
    val forma = RoundedCornerShape(4.dp)

    Box(
        modifier = modifier
            .size(tamanho)
            .clip(forma)
            .background(fundo)
            .border(2.dp, corBorda, forma),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letra?.toString().orEmpty(),
            color = corTexto,
            fontWeight = FontWeight.Bold,
            fontSize = (tamanho.value * 0.5f).sp
        )
    }
}