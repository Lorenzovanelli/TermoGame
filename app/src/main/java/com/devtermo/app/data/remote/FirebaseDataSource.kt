package com.devtermo.app.data.remote

import com.devtermo.app.model.Palavra
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import java.io.IOException

class FirebaseDataSource(
    private val firestoreProvider: () -> FirebaseFirestore = { FirebaseFirestore.getInstance() }
) {
    private val palavras get() = firestoreProvider().collection(COLECAO_PALAVRAS)

    fun novoIdPalavra(): String = palavras.document().id

    suspend fun salvarPalavra(remoteId: String, palavra: Palavra) {
        val dados = mapOf(
            "palavra" to palavra.palavra,
            "categoria" to palavra.categoria,
            "dificuldade" to palavra.dificuldade,
            "dica" to palavra.dica
        )
        comLimite { palavras.document(remoteId).set(dados).await() }
    }

    suspend fun excluirPalavra(remoteId: String) {
        comLimite { palavras.document(remoteId).delete().await() }
    }

    suspend fun buscarPalavras(): List<Palavra> = comLimite {
        palavras.get(Source.SERVER).await().documents.mapNotNull { doc ->
            val texto = doc.getString("palavra") ?: return@mapNotNull null
            Palavra(
                palavra = texto,
                categoria = doc.getString("categoria").orEmpty(),
                dificuldade = doc.getString("dificuldade").orEmpty(),
                dica = doc.getString("dica").orEmpty(),
                remoteId = doc.id,
                sincronizado = true
            )
        }
    }

    private suspend fun <T> comLimite(bloco: suspend () -> T): T =
        try {
            withTimeout(LIMITE_MS) { bloco() }
        } catch (e: TimeoutCancellationException) {
            throw IOException("Sem resposta do Firestore.", e)
        }

    private companion object {
        const val COLECAO_PALAVRAS = "palavras"
        const val LIMITE_MS = 10_000L
    }
}
