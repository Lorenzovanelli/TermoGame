package com.devtermo.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.devtermo.app.R
import com.devtermo.app.model.Categorias
import com.devtermo.app.model.Dificuldades
import com.devtermo.app.ui.components.DevTermoLogo
import com.devtermo.app.ui.theme.DevTermoColors
import com.devtermo.app.viewmodel.ProfileUiState

/** Tela 1 - Responsavel: Integrante 2. Logo, resumo do jogador e selecao de categoria e dificuldade. */
@Composable
fun HomeScreen(
    state: ProfileUiState,
    onIniciar: (categoria: String, dificuldade: String) -> Unit,
    onRanking: () -> Unit
) {
    var categoria by rememberSaveable { mutableStateOf(Categorias.todas.first()) }
    var dificuldade by rememberSaveable { mutableStateOf(Dificuldades.todas.first()) }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DevTermoLogo()
        Text(stringResource(R.string.app_slogan), style = MaterialTheme.typography.bodyMedium)

        ResumoDoJogador(state)

        Text(
            text = stringResource(R.string.home_categoria),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth()
        )
        Categorias.todas.forEach { opcao ->
            OpcaoDeCategoria(
                texto = opcao,
                selecionada = opcao == categoria,
                onSelecionar = { categoria = opcao }
            )
        }

        Text(
            text = stringResource(R.string.home_dificuldade),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth()
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Dificuldades.todas.forEach { opcao ->
                FilterChip(
                    selected = opcao == dificuldade,
                    onClick = { dificuldade = opcao },
                    label = { Text(opcao) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Button(onClick = { onIniciar(categoria, dificuldade) }, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.home_iniciar))
        }
        OutlinedButton(onClick = onRanking, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.nav_ranking))
        }
    }
}

@Composable
private fun ResumoDoJogador(state: ProfileUiState) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(state.jogador, style = MaterialTheme.typography.titleMedium)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Contador(state.vitorias, stringResource(R.string.home_vitorias), DevTermoColors.Correto)
                Contador(state.derrotas, stringResource(R.string.home_derrotas), MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
private fun Contador(valor: Int, rotulo: String, cor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = valor.toString(),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = cor
        )
        Text(rotulo, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun OpcaoDeCategoria(texto: String, selecionada: Boolean, onSelecionar: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp)
            .selectable(selected = selecionada, onClick = onSelecionar, role = Role.RadioButton),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selecionada, onClick = null)
        Text(texto, modifier = Modifier.padding(start = 8.dp))
    }
}