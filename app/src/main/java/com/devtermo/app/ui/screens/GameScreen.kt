package com.devtermo.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.devtermo.app.R
import com.devtermo.app.ui.components.GameGrid
import com.devtermo.app.ui.components.Keyboard
import com.devtermo.app.ui.components.ScreenTitle
import com.devtermo.app.viewmodel.GameStatus
import com.devtermo.app.viewmodel.GameUiState

/** Tela 2 - Responsavel: Integrante 2. Apenas exibe o GameUiState e dispara eventos. */
@Composable
fun GameScreen(
    state: GameUiState,
    onLetra: (Char) -> Unit,
    onApagar: () -> Unit,
    onConfirmar: () -> Unit,
    onSair: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScreenTitle(stringResource(R.string.game_titulo))

        when {
            state.isLoading -> Text("...")
            state.status == GameStatus.AGUARDANDO -> {
                Text(state.erro ?: "")
                OutlinedButton(onClick = onSair) { Text(stringResource(R.string.form_cancelar)) }
            }
            else -> {
                Text(stringResource(R.string.game_dica, state.dica))
                Text(stringResource(R.string.game_tentativas_restantes, state.tentativasRestantes))
                GameGrid(state)
                state.erro?.let { Text(it) }
                when (state.status) {
                    GameStatus.VITORIA -> Text("${stringResource(R.string.game_vitoria)} (${state.pontuacao})")
                    GameStatus.DERROTA -> Text(stringResource(R.string.game_derrota, state.palavraSecreta))
                    else -> Keyboard(onLetra = onLetra, onApagar = onApagar, onConfirmar = onConfirmar)
                }
                if (state.status != GameStatus.JOGANDO) {
                    OutlinedButton(onClick = onSair) { Text(stringResource(R.string.nav_inicio)) }
                }
            }
        }
    }
}
