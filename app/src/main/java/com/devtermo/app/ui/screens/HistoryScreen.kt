package com.devtermo.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.devtermo.app.R
import com.devtermo.app.ui.components.ScreenTitle
import com.devtermo.app.viewmodel.ProfileUiState
import java.text.DateFormat
import java.util.Date

/** Responsavel: Integrante 6. TODO: visual dos itens e filtros complementares. */
@Composable
fun HistoryScreen(state: ProfileUiState) {
    val formato = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        ScreenTitle(stringResource(R.string.history_titulo))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.historico, key = { it.id }) { partida ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("${partida.resultado} (${partida.tentativas} tentativas)")
                    Text("${partida.pontuacao} pts | ${formato.format(Date(partida.data))}")
                }
            }
        }
    }
}
