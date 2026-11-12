package com.devtermo.app.logic

import com.devtermo.app.model.Partida
import com.devtermo.app.model.Resultados
import org.junit.Assert.assertEquals
import org.junit.Test

class StatisticsCalculatorTest {

    private fun partida(resultado: String, tentativas: Int, pontos: Int) =
        Partida(palavraId = 1, jogador = "Ana", resultado = resultado,
            tentativas = tentativas, pontuacao = pontos, data = 0L)

    @Test
    fun calculaTaxaMediaEPontuacao() {
        val partidas = listOf(
            partida(Resultados.VITORIA, 3, 200),
            partida(Resultados.DERROTA, 6, 0),
            partida(Resultados.VITORIA, 5, 100)
        )
        val e = StatisticsCalculator.calcular(partidas)
        assertEquals(3, e.totalPartidas)
        assertEquals(2, e.vitorias)
        assertEquals(1, e.derrotas)
        assertEquals(0.6666667f, e.taxaDeAcerto, 0.0001f)
        assertEquals(4f, e.mediaTentativasVitoria, 0.0001f)
        assertEquals(300, e.pontuacaoTotal)
    }

    @Test
    fun listaVaziaDaZeros() {
        assertEquals(EstatisticasJogador(), StatisticsCalculator.calcular(emptyList()))
    }
}
