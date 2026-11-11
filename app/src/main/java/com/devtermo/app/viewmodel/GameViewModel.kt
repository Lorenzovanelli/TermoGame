package com.devtermo.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devtermo.app.data.repository.AppResult
import com.devtermo.app.data.repository.MatchRepository
import com.devtermo.app.data.repository.SessionRepository
import com.devtermo.app.data.repository.WordRepository
import com.devtermo.app.logic.GameRules
import com.devtermo.app.logic.WordValidator
import com.devtermo.app.model.Partida
import com.devtermo.app.model.Resultados
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Responsavel: Integrante 3. Controla a partida. Nao acessa DAO nem Firebase. */
class GameViewModel(
    private val wordRepository: WordRepository,
    private val matchRepository: MatchRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    // Guardado para cancelar um sorteio ainda em andamento se o jogador iniciar outra partida.
    private var inicioJob: Job? = null

    fun iniciarPartida(categoria: String, dificuldade: String) {
        inicioJob?.cancel()
        inicioJob = viewModelScope.launch {
            // Zera o estado ja no inicio: sem isso, uma falha no sorteio mostraria o tabuleiro da partida anterior.
            _uiState.update {
                GameUiState(categoria = categoria, dificuldade = dificuldade, isLoading = true)
            }
            when (val r = wordRepository.sortearParaPartida(categoria, dificuldade)) {
                is AppResult.Success -> _uiState.update {
                    GameUiState(
                        status = GameStatus.JOGANDO,
                        categoria = categoria,
                        dificuldade = dificuldade,
                        palavraId = r.data.id,
                        palavraSecreta = WordValidator.normalizar(r.data.palavra),
                        dica = r.data.dica,
                        maxTentativas = GameRules.maxTentativas(dificuldade)
                    )
                }
                is AppResult.Error -> _uiState.update { it.copy(isLoading = false, erro = r.mensagem) }
            }
        }
    }

    fun onLetra(letra: Char) {
        // Aceita so letras; "é" vindo do teclado fisico vira "E" (mesma normalizacao do validador).
        val normalizada = WordValidator.normalizar(letra.toString())
        if (normalizada.length != 1 || !normalizada[0].isLetter()) return

        _uiState.update { s ->
            if (s.status != GameStatus.JOGANDO || s.tentativaAtual.length >= s.tamanhoPalavra) s
            else s.copy(tentativaAtual = s.tentativaAtual + normalizada, erro = null)
        }
    }

    fun onApagar() {
        _uiState.update { s ->
            if (s.status != GameStatus.JOGANDO) s
            else s.copy(tentativaAtual = s.tentativaAtual.dropLast(1), erro = null)
        }
    }

    fun confirmarTentativa() {
        val s = _uiState.value
        if (s.status != GameStatus.JOGANDO) return
        if (s.tentativaAtual.length != s.tamanhoPalavra) {
            _uiState.update { it.copy(erro = GameMensagens.palavraIncompleta(s.tamanhoPalavra)) }
            return
        }

        val estados = WordValidator.avaliar(s.tentativaAtual, s.palavraSecreta)
        val tentativas = s.tentativas + Tentativa(s.tentativaAtual, estados)
        val venceu = GameRules.acertou(estados)
        val acabou = GameRules.terminou(venceu, tentativas.size, s.maxTentativas)
        val pontos = if (acabou) {
            GameRules.calcularPontuacao(venceu, tentativas.size, s.maxTentativas, s.dificuldade)
        } else 0

        _uiState.update {
            it.copy(
                tentativas = tentativas,
                tentativaAtual = "",
                pontuacao = pontos,
                erro = null,
                status = when {
                    venceu -> GameStatus.VITORIA
                    acabou -> GameStatus.DERROTA
                    else -> GameStatus.JOGANDO
                }
            )
        }

        if (acabou) registrarPartida(s.palavraId, venceu, tentativas.size, pontos)
    }

    private fun registrarPartida(palavraId: Int, venceu: Boolean, tentativas: Int, pontos: Int) {
        viewModelScope.launch {
            val partida = Partida(
                palavraId = palavraId,
                jogador = sessionRepository.jogador.value,
                resultado = if (venceu) Resultados.VITORIA else Resultados.DERROTA,
                tentativas = tentativas,
                pontuacao = pontos,
                data = System.currentTimeMillis()
            )
            when (val r = matchRepository.registrar(partida)) {
                is AppResult.Success -> _uiState.update {
                    it.copy(mensagemSucesso = GameMensagens.PARTIDA_REGISTRADA)
                }
                is AppResult.Error -> _uiState.update { it.copy(erro = r.mensagem) }
            }
        }
    }

    fun limparMensagens() {
        _uiState.update { it.copy(erro = null, mensagemSucesso = null) }
    }
}
