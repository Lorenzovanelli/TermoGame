package com.devtermo.app.viewmodel

import org.junit.Assert.assertEquals
import org.junit.Test

/** Responsavel: Integrante 3. */
class GameMensagensTest {

    @Test
    fun usaSingularParaUmaLetra() {
        assertEquals("A palavra precisa ter 1 letra.", GameMensagens.palavraIncompleta(1))
    }

    @Test
    fun usaPluralParaMaisDeUmaLetra() {
        assertEquals("A palavra precisa ter 6 letras.", GameMensagens.palavraIncompleta(6))
    }
}
