package com.devtermo.app.data.repository

import com.devtermo.app.data.local.entity.PalavraEntity
import com.devtermo.app.data.local.entity.PartidaEntity
import com.devtermo.app.model.Palavra
import com.devtermo.app.model.Partida

/** Entidades do Room nunca chegam a UI: o Repository converte para modelos de dominio. */
fun PalavraEntity.toDomain() = Palavra(id, palavra, categoria, dificuldade, dica, remoteId, sincronizado)

fun Palavra.toEntity() = PalavraEntity(id, palavra, categoria, dificuldade, dica, remoteId, sincronizado)

fun PartidaEntity.toDomain() = Partida(id, palavraId, jogador, resultado, tentativas, pontuacao, data)

fun Partida.toEntity() = PartidaEntity(id, palavraId, jogador, resultado, tentativas, pontuacao, data)
