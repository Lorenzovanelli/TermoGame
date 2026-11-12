package com.devtermo.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.devtermo.app.R
import com.devtermo.app.ui.components.ScreenTitle
import com.devtermo.app.viewmodel.ProfileUiState

/** Tela 3 - Responsavel: Integrante 6. Stateless: recebe apenas o estado. */
@Composable
fun RankingScreen(state: ProfileUiState) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        ScreenTitle(stringResource(R.string.ranking_titulo))
        state.erro?.let { mensagem ->
            Text(mensagem, color = MaterialTheme.colorScheme.error)
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.ranking, key = { it.jogador }) { item ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("${item.posicao}. ${item.jogador}")
                    Text("${item.pontuacao} pts | ${item.vitorias} vitórias")
                }
            }
        }
    }
}
