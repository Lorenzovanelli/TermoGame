package com.devtermo.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.devtermo.app.data.local.entity.PalavraEntity
import kotlinx.coroutines.flow.Flow

/** Responsavel: Integrante 4. Somente consultas e operacoes simples, sem regra de negocio. */
@Dao
interface PalavraDao {

    @Query("SELECT * FROM palavras ORDER BY palavra ASC")
    fun observarTodas(): Flow<List<PalavraEntity>>

    @Query("SELECT * FROM palavras WHERE categoria = :categoria AND dificuldade = :dificuldade")
    suspend fun listarPorCategoriaEDificuldade(categoria: String, dificuldade: String): List<PalavraEntity>

    @Query("SELECT * FROM palavras WHERE id = :id")
    suspend fun buscarPorId(id: Int): PalavraEntity?

    @Query("SELECT * FROM palavras WHERE sincronizado = 0")
    suspend fun listarPendentes(): List<PalavraEntity>

    @Query("SELECT * FROM palavras WHERE remoteId = :remoteId LIMIT 1")
    suspend fun buscarPorRemoteId(remoteId: String): PalavraEntity?

    @Query("SELECT COUNT(*) FROM palavras")
    suspend fun contar(): Int

    @Query("SELECT COUNT(*) FROM palavras")
    fun observarTotal(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(palavra: PalavraEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirTodas(palavras: List<PalavraEntity>)

    @Update
    suspend fun atualizar(palavra: PalavraEntity)

    @Query("DELETE FROM palavras WHERE id = :id")
    suspend fun excluirPorId(id: Int)
}
