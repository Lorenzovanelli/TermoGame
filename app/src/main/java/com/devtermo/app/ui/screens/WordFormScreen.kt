package com.devtermo.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.devtermo.app.R
import com.devtermo.app.model.Categorias
import com.devtermo.app.model.Dificuldades
import com.devtermo.app.model.Palavra
import com.devtermo.app.ui.components.ScreenTitle
import com.devtermo.app.viewmodel.AdminUiState

@Composable
fun WordFormScreen(
    state: AdminUiState,
    onSalvar: (Palavra) -> Unit,
    onCancelar: () -> Unit
) {
    val original = state.palavraEmEdicao
    var texto by remember(original) { mutableStateOf(original?.palavra.orEmpty()) }
    var categoria by remember(original) { mutableStateOf(original?.categoria ?: Categorias.todas.first()) }
    var dificuldade by remember(original) { mutableStateOf(original?.dificuldade ?: Dificuldades.FACIL) }
    var dica by remember(original) { mutableStateOf(original?.dica.orEmpty()) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ScreenTitle(stringResource(R.string.form_titulo))
        OutlinedTextField(value = texto, onValueChange = { texto = it }, label = { Text("Palavra") },
            singleLine = true, modifier = Modifier.fillMaxWidth())
        CampoMenu(rotulo = "Categoria", valor = categoria, opcoes = Categorias.todas, onSelecionar = { categoria = it })
        CampoMenu(rotulo = "Dificuldade", valor = dificuldade, opcoes = Dificuldades.todas, onSelecionar = { dificuldade = it })
        OutlinedTextField(value = dica, onValueChange = { dica = it }, label = { Text("Dica") },
            modifier = Modifier.fillMaxWidth())
        state.erro?.let { Text(it) }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                enabled = !state.isLoading,
                onClick = {
                    onSalvar(
                        (original ?: Palavra(palavra = "", categoria = "", dificuldade = "", dica = ""))
                            .copy(palavra = texto, categoria = categoria, dificuldade = dificuldade, dica = dica)
                    )
                }
            ) { Text(stringResource(R.string.form_salvar)) }
            OutlinedButton(onClick = onCancelar) { Text(stringResource(R.string.form_cancelar)) }
        }
    }
}

@Composable
private fun CampoMenu(
    rotulo: String,
    valor: String,
    opcoes: List<String>,
    onSelecionar: (String) -> Unit
) {
    var aberto by remember { mutableStateOf(false) }
    Box {
        OutlinedButton(onClick = { aberto = true }, modifier = Modifier.fillMaxWidth()) {
            Text("$rotulo: $valor")
        }
        DropdownMenu(expanded = aberto, onDismissRequest = { aberto = false }) {
            opcoes.forEach { opcao ->
                DropdownMenuItem(
                    text = { Text(opcao) },
                    onClick = {
                        onSelecionar(opcao)
                        aberto = false
                    }
                )
            }
        }
    }
}
