package com.devtermo.app.logic

import org.junit.Assert.assertEquals
import org.junit.Test

/** Responsavel: Integrante 3 (com apoio do Integrante 6). Testes da logica pura. */
class WordValidatorTest {

    @Test
    fun palpiteIgualTodasCorretas() {
        val r = WordValidator.avaliar("JAVA", "JAVA")
        assertEquals(List(4) { LetterState.CORRECT }, r)
    }

    @Test
    fun letraRepetidaNaoMarcaALemDaQuantidade() {
        val r = WordValidator.avaliar("AAAA", "JAVA")
        assertEquals(
            listOf(LetterState.ABSENT, LetterState.CORRECT, LetterState.ABSENT, LetterState.CORRECT),
            r
        )
    }

    @Test
    fun letrasNaPosicaoErrada() {
        val r = WordValidator.avaliar("AJAX", "JAVA")
        assertEquals(
            listOf(LetterState.PRESENT, LetterState.PRESENT, LetterState.PRESENT, LetterState.ABSENT),
            r
        )
    }

    @Test
    fun normalizaAcentosEMaiusculas() {
        assertEquals("ACAI", WordValidator.normalizar("açaí"))
    }
}
