package com.devtermo.app.viewmodel

/**
 * Responsavel: Integrante 3.
 * O ViewModel nao tem Context e por isso nao le o strings.xml. Os textos da partida ficam
 * concentrados aqui, em um unico lugar, ate o grupo decidir como levar isso para o strings.xml.
 */
object GameMensagens {
    const val PARTIDA_REGISTRADA = "Partida registrada no histórico."

    // Singular para palavras de 1 letra (ex.: "C"), pois o tamanho da palavra e variavel.
    fun palavraIncompleta(tamanho: Int): String =
        if (tamanho == 1) "A palavra precisa ter 1 letra."
        else "A palavra precisa ter $tamanho letras."
}
