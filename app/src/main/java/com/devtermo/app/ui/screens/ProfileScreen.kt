package com.devtermo.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.devtermo.app.R
import com.devtermo.app.ui.components.ScreenTitle
import com.devtermo.app.viewmodel.ProfileUiState

/** Tela 4 - Responsavel: Integrante 6. TODO: preferencias do jogador. */
@Composable
fun ProfileScreen(state: ProfileUiState, onSair: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ScreenTitle(stringResource(R.string.profile_titulo))
        Text(state.jogador)
        Text("Partidas: ${state.partidasJogador}")
        Text("Vitórias: ${state.vitorias}")
        Text("Derrotas: ${state.derrotas}")
        OutlinedButton(onClick = onSair) { Text("Sair") }
    }
}
