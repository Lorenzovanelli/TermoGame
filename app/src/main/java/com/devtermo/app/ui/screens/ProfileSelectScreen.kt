package com.devtermo.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.devtermo.app.R
import com.devtermo.app.ui.components.DevTermoLogo

/** Tela 0 - Responsavel: Integrante 1. Identificacao de perfil (controle demonstrativo). */
@Composable
fun ProfileSelectScreen(onJogador: () -> Unit, onAdmin: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DevTermoLogo()
        Text(stringResource(R.string.app_name), style = MaterialTheme.typography.displayMedium)
        Text(stringResource(R.string.app_slogan), style = MaterialTheme.typography.bodyLarge)
        Button(onClick = onJogador, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.perfil_jogador))
        }
        OutlinedButton(onClick = onAdmin, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.perfil_admin))
        }
    }
}
