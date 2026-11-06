package com.devtermo.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.devtermo.app.R

private val linhasTeclado = listOf("QWERTYUIOP", "ASDFGHJKL", "ZXCVBNM")

/** Responsavel: Integrante 2. Teclado virtual. Nao conhece ViewModel: so recebe lambdas. */
@Composable
fun Keyboard(
    onLetra: (Char) -> Unit,
    onApagar: () -> Unit,
    onConfirmar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        linhasTeclado.forEach { linha ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                linha.forEach { letra ->
                    OutlinedButton(
                        onClick = { onLetra(letra) },
                        modifier = Modifier.weight(1f).height(48.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) { Text(letra.toString()) }
                }
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
