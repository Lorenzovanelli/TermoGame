package com.devtermo.app.viewmodel

import com.devtermo.app.logic.LetterState
import com.devtermo.app.model.Categorias
import com.devtermo.app.model.Dificuldades

enum class GameStatus { AGUARDANDO, JOGANDO, VITORIA, DERROTA }

data class Tentativa(val palpite: String, val estados: List<LetterState>)

/** Responsavel: Integrante 3. Estado da funcionalidade de partida. */
data class GameUiState(
    val status: GameStatus = GameStatus.AGUARDANDO,
    val categoria: String = Categorias.LINGUAGENS,
    val dificuldade: String = Dificuldades.FACIL,
    val palavraId: Int = 0,
    val palavraSecreta: String = "",
    val dica: String = "",
    val tentativas: List<Tentativa> = emptyList(),
    val tentativaAtual: String = "",
    val maxTentativas: Int = 6,
    val pontuacao: Int = 0,
    val isLoading: Boolean = false,
    val erro: String? = null,
    val mensagemSucesso: String? = null
) {
    val tentativasRestantes: Int get() = maxTentativas - tentativas.size
    val tamanhoPalavra: Int get() = palavraSecreta.length
}
