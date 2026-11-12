package com.devtermo.app.viewmodel

import com.devtermo.app.model.Partida
import com.devtermo.app.model.Perfil
import com.devtermo.app.model.RankingItem

/** Responsavel: Integrante 6. Perfil ativo, historico, ranking e estatisticas. */
data class ProfileUiState(
    val perfil: Perfil? = null,
    val jogador: String = "Jogador",
    val partidasJogador: Int = 0,
    val vitorias: Int = 0,
    val derrotas: Int = 0,
    val taxaAcerto: Float = 0f,            // do jogador ativo, 0.0 a 1.0
    val pontuacaoTotal: Int = 0,           // do jogador ativo
    val mediaTentativasVitoria: Float = 0f,
    val historico: List<Partida> = emptyList(),
    val ranking: List<RankingItem> = emptyList(),
    val totalPartidas: Int = 0,            // geral (todos os jogadores), usado pelo painel admin
    val isLoading: Boolean = false,
    val erro: String? = null
)
