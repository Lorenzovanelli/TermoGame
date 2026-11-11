package com.devtermo.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devtermo.app.viewmodel.GameStatus
import com.devtermo.app.viewmodel.GameUiState


@Composable
fun GameGrid(state: GameUiState, modifier: Modifier = Modifier) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        val espaco = 6.dp
        val colunas = state.tamanhoPalavra.coerceAtLeast(1)
        val tamanho = ((maxWidth - espaco * (colunas - 1)) / colunas).coerceIn(0.dp, 48.dp)

        Column(verticalArrangement = Arrangement.spacedBy(espaco)) {
            for (linha in 0 until state.maxTentativas) {
                Row(horizontalArrangement = Arrangement.spacedBy(espaco)) {
                    val tentativa = state.tentativas.getOrNull(linha)
                    val ehLinhaAtual = linha == state.tentativas.size && state.status == GameStatus.JOGANDO
                    for (coluna in 0 until state.tamanhoPalavra) {
                        when {
                            tentativa != null -> LetterTile(
                                letra = tentativa.palpite[coluna],
                                estado = tentativa.estados[coluna],
                                tamanho = tamanho
                            )
                            ehLinhaAtual -> LetterTile(
                                letra = state.tentativaAtual.getOrNull(coluna),
                                estado = null,
                                tamanho = tamanho
                            )
                            else -> LetterTile(letra = null, estado = null, tamanho = tamanho)
                        }
                    }
                }
            }
        }
    }
}
