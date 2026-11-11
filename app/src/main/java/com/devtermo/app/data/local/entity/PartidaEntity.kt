package com.devtermo.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Responsavel: Integrante 4. Historico de partidas.
 * Ao excluir uma palavra, a partida e mantida e palavraId vira null (preserva historico e ranking).
 */
@Entity(
    tableName = "partidas",
    foreignKeys = [
        ForeignKey(
            entity = PalavraEntity::class,
            parentColumns = ["id"],
            childColumns = ["palavraId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("palavraId"), Index("jogador")]
)
data class PartidaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val palavraId: Int?,
    val jogador: String,
    val resultado: String,
    val tentativas: Int,
    val pontuacao: Int,
    val data: Long
)