package com.devtermo.app.logic

import com.devtermo.app.logic.LetterState.ABSENT
import com.devtermo.app.logic.LetterState.CORRECT
import com.devtermo.app.logic.LetterState.PRESENT
import org.junit.Assert.assertEquals
import org.junit.Test

class WordValidatorCasosExtraTest {

    @Test
    fun palavraLongaTodaCorreta() {
        assertEquals(List(6) { CORRECT }, WordValidator.avaliar("kotlin", "KOTLIN"))
    }

    @Test
    fun letraAusenteNaoAfetaAsDemais() {
        assertEquals(
            listOf(CORRECT, ABSENT, CORRECT, CORRECT, CORRECT, CORRECT),
            WordValidator.avaliar("SPRING", "STRING")
        )
    }

    @Test
    fun letraRepetidaMisturandoEstados() {
        assertEquals(
            listOf(PRESENT, PRESENT, CORRECT, ABSENT, ABSENT),
            WordValidator.avaliar("ERROR", "TERRA")
        )
    }

    @Test
    fun ignoraAcentosNaComparacao() {
        assertEquals(List(4) { CORRECT }, WordValidator.avaliar("acao", "AÇÃO"))
    }

    @Test
    fun normalizaRemoveEspacosDasPontas() {
        assertEquals("CAFE", WordValidator.normalizar("  café "))
    }

    @Test(expected = IllegalArgumentException::class)
    fun tamanhosDiferentesLancamExcecao() {
        WordValidator.avaliar("JAVA", "SWIFT")
    }
}
