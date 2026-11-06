package com.devtermo.app.data.remote

import com.devtermo.app.model.Palavra
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Responsavel: Integrante 5.
 * Apenas le e escreve no Firestore. Nao conhece Room, ViewModel nem UI.
 * Requer app/google-services.json configurado. As excecoes sobem para o Repository tratar.
 */
class FirebaseDataSource(
    // Acesso preguicoso: sem google-services.json o erro acontece na chamada (tratado no Repository), nao na abertura do app.
    private val firestoreProvider: () -> FirebaseFirestore = { FirebaseFirestore.getInstance() }
) {
    private val palavras get() = firestoreProvider().collection(COLECAO_PALAVRAS)

    /** Cria ou atualiza a palavra e devolve o id do documento. */
    suspend fun salvarPalavra(palavra: Palavra): String {
        val dados = mapOf(
            "palavra" to palavra.palavra,
            "categoria" to palavra.categoria,
            "dificuldade" to palavra.dificuldade,
            "dica" to palavra.dica
        )
        val id = palavra.remoteId
        return if (id == null) {
            palavras.add(dados).await().id
        } else {
            palavras.document(id).set(dados).await()
            id
        }
    }

    suspend fun excluirPalavra(remoteId: String) {
        palavras.document(remoteId).delete().await()
    }

    suspend fun buscarPalavras(): List<Palavra> =
        palavras.get().await().documents.mapNotNull { doc ->
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

    // TODO [Integrante 5/6]: ranking compartilhado (colecao "ranking") quando o grupo confirmar a decisao.

    private companion object {
        const val COLECAO_PALAVRAS = "palavras"
    }
}
