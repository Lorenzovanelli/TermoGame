package com.devtermo.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Titulo padrao das telas. */
@Composable
fun ScreenTitle(texto: String, modifier: Modifier = Modifier) {
    Text(
        text = texto,
        style = MaterialTheme.typography.headlineSmall,
        modifier = modifier.fillMaxWidth().padding(bottom = 12.dp)
    )
}

/** Linha de mensagem de erro ou sucesso (Integrante 6 pode evoluir para Snackbar). */
@Composable
fun MensagemStatus(erro: String?, sucesso: String?, onFechar: () -> Unit) {
    val texto = erro ?: sucesso ?: return
    val cor = if (erro != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(text = texto, color = cor)
        androidx.compose.material3.TextButton(onClick = onFechar) { Text("OK") }
    }
}
