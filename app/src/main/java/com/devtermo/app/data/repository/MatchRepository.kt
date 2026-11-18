package com.devtermo.app.data.repository

import com.devtermo.app.data.local.dao.PartidaDao
import com.devtermo.app.model.Partida
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class MatchRepository(private val dao: PartidaDao) {

    val historico: Flow<List<Partida>> = dao.observarTodas()
        .map { lista -> lista.map { it.toDomain() } }
        .catch { emit(emptyList()) }

    fun historicoDoJogador(jogador: String): Flow<List<Partida>> =
        dao.observarPorJogador(jogador)
            .map { lista -> lista.map { it.toDomain() } }
            .catch { emit(emptyList()) }

    suspend fun registrar(partida: Partida): AppResult<Unit> =
        safeCall("Não foi possível registrar a partida.") { dao.inserir(partida.toEntity()); Unit }

}
