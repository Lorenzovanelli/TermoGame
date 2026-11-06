package com.devtermo.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Responsavel: Integrante 4. Historico de partidas.
 * DECISAO PENDENTE: chave estrangeira para PalavraEntity e comportamento ao excluir uma palavra
 * que ja tem partidas (preservar historico ou apagar em cascata).
 */
@Entity(tableName = "partidas", indices = [Index("palavraId"), Index("jogador")])
data class PartidaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val palavraId: Int,
    val jogador: String,
    val resultado: String,
    val tentativas: Int,
    val pontuacao: Int,
    val data: Long
)
