package com.devtermo.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devtermo.app.data.repository.MatchRepository
import com.devtermo.app.data.repository.SessionRepository
import com.devtermo.app.logic.StatisticsCalculator
import com.devtermo.app.model.Partida
import com.devtermo.app.model.Perfil
import com.devtermo.app.model.RankingItem
import com.devtermo.app.model.Resultados
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Responsavel: Integrante 6 (sessao: Integrante 1).
 * O ViewModel aplica filter, groupBy, map e sortedByDescending nas listas.
 * Os calculos de taxa e pontuacao ficam em logic (StatisticsCalculator).
 */
class ProfileViewModel(
    private val matchRepository: MatchRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                matchRepository.historico,
                sessionRepository.perfil,
                sessionRepository.jogador
            ) { todas, perfil, jogador -> montarEstado(todas, perfil, jogador) }
                .catch { _uiState.update { it.copy(erro = MENSAGEM_ERRO) } }
                .collect { estado -> _uiState.value = estado }
        }
    }

    private fun montarEstado(todas: List<Partida>, perfil: Perfil?, jogador: String): ProfileUiState {
        val minhas = todas.filter { it.jogador == jogador }
        val estatisticas = StatisticsCalculator.calcular(minhas)
        return ProfileUiState(
            perfil = perfil,
            jogador = jogador,
            partidasJogador = estatisticas.totalPartidas,
            vitorias = estatisticas.vitorias,
            derrotas = estatisticas.derrotas,
            taxaAcerto = estatisticas.taxaDeAcerto,
            pontuacaoTotal = estatisticas.pontuacaoTotal,
            mediaTentativasVitoria = estatisticas.mediaTentativasVitoria,
            historico = minhas,
            ranking = montarRanking(todas),
            totalPartidas = todas.size
        )
    }

    /** groupBy por jogador, map para somar pontos, sortedByDescending para ordenar. */
    private fun montarRanking(todas: List<Partida>): List<RankingItem> =
        todas
            .groupBy { it.jogador }
            .map { (nome, partidas) ->
                RankingItem(
                    posicao = 0,
                    jogador = nome,
                    pontuacao = partidas.sumOf { it.pontuacao },
                    vitorias = partidas.count { it.resultado == Resultados.VITORIA }
                )
            }
            .sortedByDescending { it.pontuacao }
            .mapIndexed { indice, item -> item.copy(posicao = indice + 1) }

    fun entrar(perfil: Perfil, nomeJogador: String = "Jogador") = sessionRepository.entrar(perfil, nomeJogador)

    fun sair() = sessionRepository.sair()

    fun limparErro() = _uiState.update { it.copy(erro = null) }

    private companion object {
        const val MENSAGEM_ERRO = "Não foi possível carregar o perfil."
    }
}
