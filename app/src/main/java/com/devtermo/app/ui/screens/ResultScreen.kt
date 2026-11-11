package com.devtermo.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devtermo.app.R
import com.devtermo.app.ui.components.GameGrid
import com.devtermo.app.ui.theme.DevTermoColors
import com.devtermo.app.viewmodel.GameStatus
import com.devtermo.app.viewmodel.GameUiState

/** Tela de resultado - Responsavel: Integrante 2. Exibida pela GameScreen quando a partida termina. */
@Composable
fun ResultScreen(state: GameUiState, onSair: () -> Unit) {
    val venceu = state.status == GameStatus.VITORIA

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = if (venceu) "🎉" else "😕", fontSize = 48.sp)
        Text(
            text = if (venceu) {
                stringResource(R.string.game_vitoria)
            } else {
                stringResource(R.string.game_derrota, state.palavraSecreta)
            },
            style = MaterialTheme.typography.headlineSmall,
            color = if (venceu) DevTermoColors.Correto else MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
        if (venceu) {
            Text(stringResource(R.string.result_pontuacao, state.pontuacao), style = MaterialTheme.typography.titleMedium)
        }
        Text(stringResource(R.string.result_tentativas, state.tentativas.size, state.maxTentativas))
        GameGrid(state)
        state.erro?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        Button(onClick = onSair, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.nav_inicio))
        }
    }
}