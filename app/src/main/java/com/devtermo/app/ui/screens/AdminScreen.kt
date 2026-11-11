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
import com.devtermo.app.ui.components.MensagemStatus
import com.devtermo.app.ui.components.ScreenTitle
import com.devtermo.app.viewmodel.AdminUiState
import com.devtermo.app.viewmodel.ProfileUiState

@Composable
fun AdminScreen(
    state: AdminUiState,
    geral: ProfileUiState,
    onGerenciar: () -> Unit,
    onSincronizar: () -> Unit,
    onLimparMensagens: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ScreenTitle(stringResource(R.string.admin_titulo))
        Text("Palavras cadastradas: ${state.totalPalavras}")
        Text("Partidas registradas: ${geral.totalPartidas}")
        MensagemStatus(state.erro, state.mensagemSucesso, onLimparMensagens)
        Button(onClick = onGerenciar, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.words_titulo))
        }
        OutlinedButton(
            onClick = onSincronizar,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sincronizar com a nuvem")
        }
    }
}
