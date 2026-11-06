package com.devtermo.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devtermo.app.data.repository.MatchRepository
import com.devtermo.app.data.repository.SessionRepository
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

/** Responsavel: Integrante 6 (sessao: Integrante 1). Demonstra filter, map e sortedByDescending. */
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
            ) { todas, perfil, jogador -> Triple(todas, perfil, jogador) }
                .catch { e -> _uiState.update { it.copy(erro = "Erro ao carregar dados: ${e.message}") } }
                .collect { (todas, perfil, jogador) ->
                    val minhas = todas.filter { it.jogador == jogador }
                    val vitoriasGerais = todas.count { it.resultado == Resultados.VITORIA }
                    val ranking = todas
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

                    _uiState.update {
                        it.copy(
                            perfil = perfil,
                            jogador = jogador,
                            partidasJogador = minhas.size,
                            vitorias = minhas.count { p -> p.resultado == Resultados.VITORIA },
                            derrotas = minhas.count { p -> p.resultado == Resultados.DERROTA },
                            historico = minhas,
                            ranking = ranking,
                            totalPartidas = todas.size,
                            taxaAcerto = if (todas.isEmpty()) 0f else vitoriasGerais.toFloat() / todas.size
                        )
                    }
                }
        }
    }

    fun entrar(perfil: Perfil, nomeJogador: String = "Jogador") = sessionRepository.entrar(perfil, nomeJogador)

    fun sair() = sessionRepository.sair()
}
