package com.devtermo.app.viewmodel

import com.devtermo.app.model.Partida
import com.devtermo.app.model.Perfil
import com.devtermo.app.model.RankingItem

data class ProfileUiState(
    val perfil: Perfil? = null,
    val jogador: String = "Jogador",
    val partidasJogador: Int = 0,
    val vitorias: Int = 0,
    val derrotas: Int = 0,
    val taxaAcerto: Float = 0f,            
    val pontuacaoTotal: Int = 0,           
    val mediaTentativasVitoria: Float = 0f,
    val historico: List<Partida> = emptyList(),
    val ranking: List<RankingItem> = emptyList(),
    val totalPartidas: Int = 0,           
    val isLoading: Boolean = false,
    val erro: String? = null
)
