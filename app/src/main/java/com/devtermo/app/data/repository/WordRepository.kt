package com.devtermo.app.data.repository

import com.devtermo.app.data.local.dao.PalavraDao
import com.devtermo.app.data.local.entity.PalavraEntity
import com.devtermo.app.data.remote.FirebaseDataSource
import com.devtermo.app.model.Categorias
import com.devtermo.app.model.Dificuldades
import com.devtermo.app.model.Palavra
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.flow.catch

/**
 * Responsaveis: Integrante 4 (parte local) e Integrante 5 (sincronizacao).
 * Room e a fonte principal. Firestore e a fonte compartilhada.
 * Nunca assume que gravar em um lado atualizou o outro: o campo sincronizado registra pendencias.
 */
class WordRepository(
    private val dao: PalavraDao,
    private val remote: FirebaseDataSource
) {
    val palavras: Flow<List<Palavra>> = dao.observarTodas()
        .map { lista -> lista.map { it.toDomain() } }
        .catch { emit(emptyList()) }

    val total: Flow<Int> = dao.observarTotal().catch { emit(0) }

    /**
     * Salva no Room e tenta enviar ao Firestore.
     * Success(true) = salvo nas duas fontes. Success(false) = salvo so local (pendente de sincronizacao).
     */
    suspend fun salvar(palavra: Palavra): AppResult<Boolean> {
        val local = when (
            val r = safeCall("Não foi possível salvar a palavra no banco local.") {
                val entidade = palavra.copy(sincronizado = false).toEntity()
                val id = if (palavra.id == 0) {
                    dao.inserir(entidade).toInt()
                } else {
                    dao.atualizar(entidade)
                    palavra.id
                }
                palavra.copy(id = id, sincronizado = false)
            }
        ) {
            is AppResult.Success -> r.data
            is AppResult.Error -> return r
        }

        return try {
            enviar(local.toEntity())
            AppResult.Success(true)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            AppResult.Success(false)
        }
    }

    private suspend fun enviar(entidade: PalavraEntity) {
        val remoteId = entidade.remoteId ?: remote.novoIdPalavra()
        val comId = entidade.copy(remoteId = remoteId)
        dao.atualizar(comId)
        remote.salvarPalavra(remoteId, comId.toDomain())
        dao.atualizar(comId.copy(sincronizado = true))
    }

    /** Exclui no Room e tenta excluir no Firestore. Success(false) = remoto nao foi atualizado. */
    suspend fun excluir(id: Int): AppResult<Boolean> {
        val remoteId = when (
            val r = safeCall("Não foi possível excluir a palavra no banco local.") {
                val idRemoto = dao.buscarPorId(id)?.remoteId
                dao.excluirPorId(id)
                idRemoto
            }
        ) {
            is AppResult.Success -> r.data
            is AppResult.Error -> return r
        }
        if (remoteId == null) return AppResult.Success(true)

        return try {
            remote.excluirPalavra(remoteId)
            AppResult.Success(true)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            AppResult.Success(false)
        }
    }

    suspend fun sincronizarPendentes(): AppResult<Int> =
        safeCall("Falha ao sincronizar com a nuvem. Verifique a conexão.") {
            val pendentes = dao.listarPendentes()
            pendentes.forEach { enviar(it) }
            pendentes.size
        }

    suspend fun importarCatalogoRemoto(): AppResult<Int> =
        safeCall("Não foi possível baixar o catálogo da nuvem.") {
            val remotas = remote.buscarPalavras()
            val locais = dao.observarTodas().first()
            var importadas = 0
            for (doc in remotas) {
                val existente = locais.firstOrNull { it.remoteId == doc.remoteId }
                    ?: locais.firstOrNull { it.remoteId == null && it.palavra == doc.palavra }
                val remota = doc.copy(id = existente?.id ?: 0).toEntity()
                when {
                    existente == null -> {
                        dao.inserir(remota)
                        importadas++
                    }
                    !existente.sincronizado ->
                        if (existente.remoteId == null) dao.atualizar(existente.copy(remoteId = doc.remoteId))
                    existente != remota -> {
                        dao.atualizar(remota)
                        importadas++
                    }
                }
            }
            importadas
        }

    /**
     * Sorteia uma palavra da categoria e dificuldade.
     * DECISAO PENDENTE: estrategia para palavras de tamanhos diferentes.
     */
    suspend fun sortearParaPartida(categoria: String, dificuldade: String): AppResult<Palavra> {
        val candidatas = when (
            val r = safeCall("Não foi possível buscar palavras no banco local.") {
                dao.listarPorCategoriaEDificuldade(categoria, dificuldade)
            }
        ) {
            is AppResult.Success -> r.data
            is AppResult.Error -> return r
        }
        if (candidatas.isEmpty()) {
            return AppResult.Error("Nenhuma palavra cadastrada para essa categoria e dificuldade.")
        }
        return AppResult.Success(candidatas.random().toDomain())
    }

    suspend fun buscarPorId(id: Int): AppResult<Palavra?> =
        safeCall("Não foi possível carregar a palavra.") { dao.buscarPorId(id)?.toDomain() }

    /** Carga inicial para o jogo funcionar na primeira abertura. O catalogo real e gerido pelo admin. */
    suspend fun semearSeVazio() {
        try {
            if (dao.contar() > 0) return
            val iniciais = listOf(
                Palavra(palavra = "JAVA", categoria = Categorias.LINGUAGENS, dificuldade = Dificuldades.FACIL, dica = "Linguagem do logo da xícara de café"),
                Palavra(palavra = "RUBY", categoria = Categorias.LINGUAGENS, dificuldade = Dificuldades.FACIL, dica = "Linguagem criada por Matz"),
                Palavra(palavra = "SWIFT", categoria = Categorias.LINGUAGENS, dificuldade = Dificuldades.MEDIO, dica = "Linguagem da Apple para iOS"),
                Palavra(palavra = "KOTLIN", categoria = Categorias.LINGUAGENS, dificuldade = Dificuldades.DIFICIL, dica = "Linguagem oficial para Android"),
                Palavra(palavra = "STRING", categoria = Categorias.CONCEITOS, dificuldade = Dificuldades.FACIL, dica = "Tipo para sequência de caracteres"),
                Palavra(palavra = "ARRAY", categoria = Categorias.CONCEITOS, dificuldade = Dificuldades.MEDIO, dica = "Coleção indexada de elementos"),
                Palavra(palavra = "CLASSE", categoria = Categorias.CONCEITOS, dificuldade = Dificuldades.DIFICIL, dica = "Molde para criar objetos"),
                Palavra(palavra = "REACT", categoria = Categorias.FRAMEWORKS, dificuldade = Dificuldades.FACIL, dica = "Biblioteca JavaScript para interfaces"),
                Palavra(palavra = "SPRING", categoria = Categorias.FRAMEWORKS, dificuldade = Dificuldades.MEDIO, dica = "Framework Java para back-end"),
                Palavra(palavra = "LARAVEL", categoria = Categorias.FRAMEWORKS, dificuldade = Dificuldades.DIFICIL, dica = "Framework PHP")
            )
            dao.inserirTodas(iniciais.map { it.toEntity() })
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            // Falha na carga inicial nao impede o uso do app.
        }
    }
}
