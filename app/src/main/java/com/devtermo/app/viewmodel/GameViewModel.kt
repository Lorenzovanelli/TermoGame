package com.devtermo.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devtermo.app.data.repository.AppResult
import com.devtermo.app.data.repository.MatchRepository
import com.devtermo.app.data.repository.SessionRepository
import com.devtermo.app.data.repository.WordRepository
import com.devtermo.app.logic.GameRules
import com.devtermo.app.logic.LetterState
import com.devtermo.app.logic.WordValidator
import com.devtermo.app.model.Partida
import com.devtermo.app.model.Resultados
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

    fun iniciarPartida(categoria: String, dificuldade: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, erro = null) }
            when (val r = wordRepository.sortearParaPartida(categoria, dificuldade)) {
                is AppResult.Success -> _uiState.value = GameUiState(
                    status = GameStatus.JOGANDO,
                    categoria = categoria,
                    dificuldade = dificuldade,
                    palavraId = r.data.id,
                    palavraSecreta = WordValidator.normalizar(r.data.palavra),
                    dica = r.data.dica,
                    maxTentativas = GameRules.maxTentativas(dificuldade)
                )
                is AppResult.Error -> _uiState.update { it.copy(isLoading = false, erro = r.mensagem) }
            }
        }
    }

    fun onLetra(letra: Char) {
        _uiState.update { s ->
            if (s.status != GameStatus.JOGANDO || s.tentativaAtual.length >= s.tamanhoPalavra) s
            else s.copy(tentativaAtual = s.tentativaAtual + letra.uppercaseChar())
        }
    }

    fun onApagar() {
        _uiState.update { s ->
            if (s.status != GameStatus.JOGANDO) s else s.copy(tentativaAtual = s.tentativaAtual.dropLast(1))
        }
    }

    fun confirmarTentativa() {
        val s = _uiState.value
        if (s.status != GameStatus.JOGANDO) return
        if (s.tentativaAtual.length != s.tamanhoPalavra) {
            _uiState.update { it.copy(erro = "A palavra precisa ter ${s.tamanhoPalavra} letras.") }
            return
        }

        val estados = WordValidator.avaliar(s.tentativaAtual, s.palavraSecreta)
        val tentativas = s.tentativas + Tentativa(s.tentativaAtual, estados)
        val venceu = estados.all { it == LetterState.CORRECT }
        val acabou = venceu || tentativas.size >= s.maxTentativas
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
                is AppResult.Success -> Unit
                is AppResult.Error -> _uiState.update { it.copy(erro = r.mensagem) }
            }
        }
    }

    fun limparMensagens() {
        _uiState.update { it.copy(erro = null, mensagemSucesso = null) }
    }
}
