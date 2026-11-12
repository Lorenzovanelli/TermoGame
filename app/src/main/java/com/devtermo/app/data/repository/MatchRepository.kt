package com.devtermo.app.data.repository

import com.devtermo.app.data.local.dao.PartidaDao
import com.devtermo.app.model.Partida
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Responsaveis: Integrante 4 (parte local) e Integrante 6 (ranking).
 * O ranking e calculado a partir das partidas no ProfileViewModel (sortedByDescending).
 */
class MatchRepository(private val dao: PartidaDao) {

    val historico: Flow<List<Partida>> = dao.observarTodas().map { lista -> lista.map { it.toDomain() } }

    fun historicoDoJogador(jogador: String): Flow<List<Partida>> =
        dao.observarPorJogador(jogador).map { lista -> lista.map { it.toDomain() } }

    suspend fun registrar(partida: Partida): AppResult<Unit> =
        safeCall("Não foi possível registrar a partida.") { dao.inserir(partida.toEntity()); Unit }

    // TODO [Integrante 6]: enviar/ler ranking compartilhado no Firestore (decisao pendente do grupo).
}
