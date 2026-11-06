package com.devtermo.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.devtermo.app.R
import com.devtermo.app.ui.components.ScreenTitle
import com.devtermo.app.viewmodel.ProfileUiState

/** Tela 1 - Responsavel: Integrante 2. TODO: selecao de categoria e dificuldade, logo, resumo visual. */
@Composable
fun HomeScreen(state: ProfileUiState, onIniciar: () -> Unit, onRanking: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ScreenTitle(stringResource(R.string.app_name))
        Text("${state.jogador}: ${state.vitorias} vitória(s), ${state.derrotas} derrota(s)")
        Button(onClick = onIniciar, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.home_iniciar))
        }
        OutlinedButton(onClick = onRanking, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.nav_ranking))
        }
    }
}
