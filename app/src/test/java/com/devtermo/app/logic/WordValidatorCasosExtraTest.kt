package com.devtermo.app.logic

import com.devtermo.app.logic.LetterState.ABSENT
import com.devtermo.app.logic.LetterState.CORRECT
import com.devtermo.app.logic.LetterState.PRESENT
import org.junit.Assert.assertEquals
import org.junit.Test

/** Responsavel: Integrante 3. Casos adicionais do validador, com palavras do catalogo do projeto. */
class WordValidatorCasosExtraTest {

    @Test
    fun palavraLongaTodaCorreta() {
        assertEquals(List(6) { CORRECT }, WordValidator.avaliar("kotlin", "KOTLIN"))
    }

    @Test
    fun letraAusenteNaoAfetaAsDemais() {
        // SPRING x STRING: o P nao existe na secreta
        assertEquals(
            listOf(CORRECT, ABSENT, CORRECT, CORRECT, CORRECT, CORRECT),
            WordValidator.avaliar("SPRING", "STRING")
        )
    }

    @Test
    fun letraRepetidaMisturandoEstados() {
        // TERRA x ERROR: so existem dois R na secreta; um deles ja foi acertado
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
