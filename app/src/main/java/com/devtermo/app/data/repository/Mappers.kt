package com.devtermo.app.data.repository

import com.devtermo.app.data.local.entity.PalavraEntity
import com.devtermo.app.data.local.entity.PartidaEntity
import com.devtermo.app.model.Palavra
import com.devtermo.app.model.Partida

/** Id usado no modelo de dominio quando a palavra da partida foi excluida (ids reais comecam em 1). */
private const val SEM_PALAVRA = 0

/** Entidades do Room nunca chegam a UI: o Repository converte para modelos de dominio. */
fun PalavraEntity.toDomain() = Palavra(id, palavra, categoria, dificuldade, dica, remoteId, sincronizado)

fun Palavra.toEntity() = PalavraEntity(id, palavra, categoria, dificuldade, dica, remoteId, sincronizado)

fun PartidaEntity.toDomain() =
    Partida(id, palavraId ?: SEM_PALAVRA, jogador, resultado, tentativas, pontuacao, data)

fun Partida.toEntity() =
    PartidaEntity(id, palavraId.takeIf { it != SEM_PALAVRA }, jogador, resultado, tentativas, pontuacao, data)