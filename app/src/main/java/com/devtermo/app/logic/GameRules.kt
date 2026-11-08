package com.devtermo.app.logic

import com.devtermo.app.model.Dificuldades

/**
 * Responsavel: Integrante 3.
 * Regras de tentativas, pontuacao e teclado. Funcoes puras, sem Android.
 * Os valores abaixo sao uma PROPOSTA inicial e precisam ser confirmados pelo grupo.
 */
object GameRules {

    private const val TENTATIVAS_FACIL = 7
    private const val TENTATIVAS_MEDIO = 6
    private const val TENTATIVAS_DIFICIL = 5
    private const val TENTATIVAS_PADRAO = 6

    private const val PONTOS_BASE = 100
    private const val PONTOS_POR_TENTATIVA_RESTANTE = 20

    private const val BONUS_FACIL = 1
    private const val BONUS_MEDIO = 2
    private const val BONUS_DIFICIL = 3
    private const val BONUS_PADRAO = 1

    fun maxTentativas(dificuldade: String): Int = when (dificuldade) {
        Dificuldades.FACIL -> TENTATIVAS_FACIL
        Dificuldades.MEDIO -> TENTATIVAS_MEDIO
        Dificuldades.DIFICIL -> TENTATIVAS_DIFICIL
        else -> TENTATIVAS_PADRAO
    }

    private fun bonusDificuldade(dificuldade: String): Int = when (dificuldade) {
        Dificuldades.FACIL -> BONUS_FACIL
        Dificuldades.MEDIO -> BONUS_MEDIO
        Dificuldades.DIFICIL -> BONUS_DIFICIL
        else -> BONUS_PADRAO
    }

    /** O palpite acertou quando todas as letras estao na posicao certa. */
    fun acertou(estados: List<LetterState>): Boolean =
        estados.isNotEmpty() && estados.all { it == LetterState.CORRECT }

    /** A partida termina ao acertar ou ao gastar todas as tentativas. */
    fun terminou(venceu: Boolean, tentativasUsadas: Int, maxTentativas: Int): Boolean =
        venceu || tentativasUsadas >= maxTentativas

    /** Derrota vale 0. Vitoria: quanto menos tentativas, mais pontos, multiplicado pela dificuldade. */
    fun calcularPontuacao(venceu: Boolean, tentativasUsadas: Int, maxTentativas: Int, dificuldade: String): Int {
        if (!venceu) return 0
        val restantes = (maxTentativas - tentativasUsadas).coerceAtLeast(0)
        return (PONTOS_BASE + restantes * PONTOS_POR_TENTATIVA_RESTANTE) * bonusDificuldade(dificuldade)
    }

    /**
     * Cor de cada tecla do teclado: vale sempre a melhor informacao ja descoberta
     * (uma letra que ja foi CORRECT nunca volta a ser PRESENT ou ABSENT).
     * Recebe pares (palpite, estados) para a logica nao depender do pacote viewmodel.
     */
    fun estadoDasTeclas(tentativas: List<Pair<String, List<LetterState>>>): Map<Char, LetterState> {
        val teclas = mutableMapOf<Char, LetterState>()
        for ((palpite, estados) in tentativas) {
            WordValidator.normalizar(palpite).forEachIndexed { i, letra ->
                val novo = estados[i]
                val atual = teclas[letra]
                if (atual == null || prioridade(novo) > prioridade(atual)) {
                    teclas[letra] = novo
                }
            }
        }
        return teclas
    }

    private fun prioridade(estado: LetterState): Int = when (estado) {
        LetterState.ABSENT -> 0
        LetterState.PRESENT -> 1
        LetterState.CORRECT -> 2
    }
}
