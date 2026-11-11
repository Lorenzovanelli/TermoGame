package com.devtermo.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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

@Composable
fun GameScreen(
    state: GameUiState,
    onLetra: (Char) -> Unit,
    onApagar: () -> Unit,
    onConfirmar: () -> Unit,
    onSair: () -> Unit
) {
    val terminou = !state.isLoading &&
            (state.status == GameStatus.VITORIA || state.status == GameStatus.DERROTA)

    if (terminou) {
        ResultScreen(state = state, onSair = onSair)
        return
    }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScreenTitle(stringResource(R.string.game_titulo))

        when {
            state.isLoading -> CircularProgressIndicator()
            state.status == GameStatus.AGUARDANDO -> {
                Text(state.erro ?: "")
                OutlinedButton(onClick = onSair) { Text(stringResource(R.string.form_cancelar)) }
            }
            else -> {
                Text(stringResource(R.string.game_dica, state.dica), style = MaterialTheme.typography.bodyLarge)
                Text(stringResource(R.string.game_tentativas_restantes, state.tentativasRestantes))
                GameGrid(state)
                state.erro?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                Keyboard(onLetra = onLetra, onApagar = onApagar, onConfirmar = onConfirmar)
            }
        }
    }
}