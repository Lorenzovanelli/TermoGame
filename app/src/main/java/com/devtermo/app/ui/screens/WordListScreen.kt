package com.devtermo.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.devtermo.app.R
import com.devtermo.app.model.Dificuldades
import com.devtermo.app.ui.components.MensagemStatus
import com.devtermo.app.ui.components.ScreenTitle
import com.devtermo.app.viewmodel.AdminUiState
@Composable
fun WordListScreen(
    state: AdminUiState,
    onBusca: (String) -> Unit,
    onCategoria: (String?) -> Unit,
    onDificuldade: (String?) -> Unit,
    onAdicionar: () -> Unit,
    onEditar: (Int) -> Unit,
    onExcluir: (Int) -> Unit,
    onLimparMensagens: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ScreenTitle(stringResource(R.string.words_titulo))
        OutlinedTextField(
            value = state.busca,
            onValueChange = onBusca,
            label = { Text(stringResource(R.string.words_buscar)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.categoriasDisponiveis, key = { it }) { categoria ->
                val selecionada = categoria == state.filtroCategoria
                FilterChip(
                    selected = selecionada,
                    onClick = { onCategoria(if (selecionada) null else categoria) },
                    label = { Text(categoria) }
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Dificuldades.todas.forEach { dificuldade ->
                val selecionada = dificuldade == state.filtroDificuldade
                FilterChip(
                    selected = selecionada,
                    onClick = { onDificuldade(if (selecionada) null else dificuldade) },
                    label = { Text(dificuldade) }
                )
            }
        }
        MensagemStatus(state.erro, state.mensagemSucesso, onLimparMensagens)
        Button(onClick = onAdicionar, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.words_adicionar))
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            items(state.palavras, key = { it.id }) { palavra ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(palavra.palavra)
                        Text("${palavra.categoria} | ${palavra.dificuldade}")
                    }
                    TextButton(onClick = { onEditar(palavra.id) }) { Text("Editar") }
                    TextButton(onClick = { onExcluir(palavra.id) }) { Text("Excluir") }
                }
            }
        }
    }
}
