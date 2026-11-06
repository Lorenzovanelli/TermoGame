package com.devtermo.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devtermo.app.viewmodel.GameStatus
import com.devtermo.app.viewmodel.GameUiState

/** Responsavel: Integrante 2. Grade de tentativas: linhas avaliadas, linha atual e linhas vazias. */
@Composable
fun GameGrid(state: GameUiState, modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        for (linha in 0 until state.maxTentativas) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                val tentativa = state.tentativas.getOrNull(linha)
                val ehLinhaAtual = linha == state.tentativas.size && state.status == GameStatus.JOGANDO
                for (coluna in 0 until state.tamanhoPalavra) {
                    when {
                        tentativa != null -> LetterTile(tentativa.palpite[coluna], tentativa.estados[coluna])
                        ehLinhaAtual -> LetterTile(state.tentativaAtual.getOrNull(coluna), null)
                        else -> LetterTile(null, null)
                    }
                }
            }
        }
    }
}
