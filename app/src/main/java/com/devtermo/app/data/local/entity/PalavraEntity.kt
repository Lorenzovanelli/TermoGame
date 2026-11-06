package com.devtermo.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Responsavel: Integrante 4. Tabela de palavras do catalogo. */
@Entity(tableName = "palavras")
data class PalavraEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val palavra: String,
    val categoria: String,
    val dificuldade: String,
    val dica: String,
    /** Id do documento no Firestore (null enquanto nao enviado). */
    val remoteId: String? = null,
    /** false = alteracao local ainda nao enviada ao Firestore. */
    val sincronizado: Boolean = false
)
