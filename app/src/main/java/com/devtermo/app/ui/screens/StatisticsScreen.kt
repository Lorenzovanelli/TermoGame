package com.devtermo.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.devtermo.app.R
import com.devtermo.app.ui.components.ScreenTitle
import com.devtermo.app.viewmodel.ProfileUiState

/** Tela 8 - Responsavel: Integrante 6. TODO: palavras mais dificeis e mais utilizadas. */
@Composable
fun StatisticsScreen(state: ProfileUiState) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ScreenTitle(stringResource(R.string.stats_titulo))
        Text("Total de partidas: ${state.totalPartidas}")
        Text("Taxa de acerto: ${(state.taxaAcerto * 100).toInt()}%")
    }
}
