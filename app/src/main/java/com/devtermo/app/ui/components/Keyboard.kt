package com.devtermo.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.devtermo.app.R

private val linhasTeclado = listOf("QWERTYUIOP", "ASDFGHJKL", "ZXCVBNM")
private const val TECLAS_NA_MAIOR_LINHA = 10

@Composable
fun Keyboard(
    onLetra: (Char) -> Unit,
    onApagar: () -> Unit,
    onConfirmar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        linhasTeclado.forEach { linha ->
            val margem = (TECLAS_NA_MAIOR_LINHA - linha.length) / 2f
            Row(modifier = Modifier.fillMaxWidth()) {
                if (margem > 0f) Spacer(modifier = Modifier.weight(margem))
                linha.forEach { letra ->
                    FilledTonalButton(
                        onClick = { onLetra(letra) },
                        modifier = Modifier.weight(1f).padding(horizontal = 2.dp).height(48.dp),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) { Text(letra.toString(), fontWeight = FontWeight.Bold) }
                }
                if (margem > 0f) Spacer(modifier = Modifier.weight(margem))
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = onApagar, modifier = Modifier.weight(1f)) {
                Text(stringResource(R.string.game_apagar))
            }
            Button(onClick = onConfirmar, modifier = Modifier.weight(1f)) {
                Text(stringResource(R.string.game_confirmar))
            }
        }
    }
}