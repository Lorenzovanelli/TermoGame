package com.devtermo.app.logic

import com.devtermo.app.model.Dificuldades

/**
 * Responsavel: Integrante 3.
 * Regras de tentativas e pontuacao. Os valores abaixo sao uma PROPOSTA inicial e
 * precisam ser confirmados pelo grupo.
 */
object GameRules {

    fun maxTentativas(dificuldade: String): Int = when (dificuldade) {
        Dificuldades.FACIL -> 7
        Dificuldades.MEDIO -> 6
        Dificuldades.DIFICIL -> 5
        else -> 6
    }

    private fun bonusDificuldade(dificuldade: String): Int = when (dificuldade) {
        Dificuldades.FACIL -> 1
        Dificuldades.MEDIO -> 2
        Dificuldades.DIFICIL -> 3
        else -> 1
    }

    /** Derrota vale 0. Vitoria: quanto menos tentativas, mais pontos, multiplicado pela dificuldade. */
    fun calcularPontuacao(venceu: Boolean, tentativasUsadas: Int, maxTentativas: Int, dificuldade: String): Int {
        if (!venceu) return 0
        val restantes = (maxTentativas - tentativasUsadas).coerceAtLeast(0)
        return (100 + restantes * 20) * bonusDificuldade(dificuldade)
    }
}
