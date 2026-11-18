package com.devtermo.app.logic

import com.devtermo.app.logic.LetterState.ABSENT
import com.devtermo.app.logic.LetterState.CORRECT
import com.devtermo.app.logic.LetterState.PRESENT
import com.devtermo.app.model.Dificuldades
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GameRulesTest {

    @Test
    fun maxTentativasPorDificuldade() {
        assertEquals(7, GameRules.maxTentativas(Dificuldades.FACIL))
        assertEquals(6, GameRules.maxTentativas(Dificuldades.MEDIO))
        assertEquals(5, GameRules.maxTentativas(Dificuldades.DIFICIL))
    }

    @Test
    fun dificuldadeDesconhecidaUsaValorPadrao() {
        assertEquals(6, GameRules.maxTentativas("qualquer coisa"))
    }

    @Test
    fun acertouSoQuandoTodasCorretas() {
        assertTrue(GameRules.acertou(listOf(CORRECT, CORRECT)))
        assertFalse(GameRules.acertou(listOf(CORRECT, PRESENT)))
        assertFalse(GameRules.acertou(emptyList()))
    }

    @Test
    fun partidaTerminaAoAcertarOuAoGastarTentativas() {
        assertTrue(GameRules.terminou(venceu = true, tentativasUsadas = 1, maxTentativas = 6))
        assertTrue(GameRules.terminou(venceu = false, tentativasUsadas = 6, maxTentativas = 6))
        assertFalse(GameRules.terminou(venceu = false, tentativasUsadas = 5, maxTentativas = 6))
    }

    @Test
    fun derrotaNaoPontua() {
        assertEquals(0, GameRules.calcularPontuacao(false, 6, 6, Dificuldades.MEDIO))
    }

    @Test
    fun vitoriaMultiplicaPelaDificuldade() {
        assertEquals(360, GameRules.calcularPontuacao(true, 2, 6, Dificuldades.MEDIO))
        assertEquals(220, GameRules.calcularPontuacao(true, 1, 7, Dificuldades.FACIL))
        assertEquals(300, GameRules.calcularPontuacao(true, 5, 5, Dificuldades.DIFICIL))
    }

    @Test
    fun acertarComMenosTentativasRendeMaisPontos() {
        val rapida = GameRules.calcularPontuacao(true, 2, 6, Dificuldades.MEDIO)
        val lenta = GameRules.calcularPontuacao(true, 6, 6, Dificuldades.MEDIO)
        assertTrue(rapida > lenta)
    }

    @Test
    fun bonusDeSobraNuncaFicaNegativo() {
        assertEquals(200, GameRules.calcularPontuacao(true, 7, 6, Dificuldades.MEDIO))
    }

    @Test
    fun tecladoGuardaAMelhorInformacaoDaLetra() {
        val tentativas = listOf(
            "ABCD" to listOf(PRESENT, ABSENT, ABSENT, ABSENT),
            "AXYZ" to listOf(CORRECT, ABSENT, ABSENT, ABSENT)
        )
        val teclas = GameRules.estadoDasTeclas(tentativas)
        assertEquals(CORRECT, teclas['A'])
        assertEquals(ABSENT, teclas['B'])
    }

    @Test
    fun tecladoNaoPioraUmaLetraJaCorreta() {
        val tentativas = listOf(
            "AXYZ" to listOf(CORRECT, ABSENT, ABSENT, ABSENT),
            "BCDA" to listOf(ABSENT, ABSENT, ABSENT, PRESENT)
        )
        assertEquals(CORRECT, GameRules.estadoDasTeclas(tentativas)['A'])
    }

    @Test
    fun tecladoSemTentativasFicaVazio() {
        assertTrue(GameRules.estadoDasTeclas(emptyList()).isEmpty())
    }
}
