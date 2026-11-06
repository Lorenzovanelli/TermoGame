package com.devtermo.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.devtermo.app.data.local.entity.PartidaEntity
import kotlinx.coroutines.flow.Flow

/** Responsavel: Integrante 4. */
@Dao
interface PartidaDao {

    @Query("SELECT * FROM partidas ORDER BY data DESC")
    fun observarTodas(): Flow<List<PartidaEntity>>

    @Query("SELECT * FROM partidas WHERE jogador = :jogador ORDER BY data DESC")
    fun observarPorJogador(jogador: String): Flow<List<PartidaEntity>>

    @Insert
    suspend fun inserir(partida: PartidaEntity): Long

    @Update
    suspend fun atualizar(partida: PartidaEntity)

    @Query("DELETE FROM partidas WHERE id = :id")
    suspend fun excluirPorId(id: Int)
}
