package com.devtermo.app.logic

import java.text.Normalizer

enum class LetterState { CORRECT, PRESENT, ABSENT }

/**
 * Responsavel: Integrante 3.
 * Funcoes puras, sem Android. Compara palpite e palavra secreta tratando letras repetidas.
 */
object WordValidator {

    /** Padroniza texto: maiusculas e sem acentos. */
    fun normalizar(texto: String): String =
        Normalizer.normalize(texto.trim(), Normalizer.Form.NFD)
            .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
            .uppercase()

    /**
     * Passagem 1: marca as letras corretas e conta as letras restantes da palavra secreta.
     * Passagem 2: marca como PRESENT apenas enquanto houver letra restante disponivel.
     */
    fun avaliar(palpite: String, secreta: String): List<LetterState> {
        val p = normalizar(palpite)
        val s = normalizar(secreta)
        require(p.length == s.length) { "Palpite e palavra secreta devem ter o mesmo tamanho" }

        val resultado = MutableList(p.length) { LetterState.ABSENT }
        val restantes = mutableMapOf<Char, Int>()

        for (i in p.indices) {
            if (p[i] == s[i]) {
                resultado[i] = LetterState.CORRECT
            } else {
                restantes[s[i]] = (restantes[s[i]] ?: 0) + 1
            }
        }
        for (i in p.indices) {
            if (resultado[i] == LetterState.CORRECT) continue
            val qtd = restantes[p[i]] ?: 0
            if (qtd > 0) {
                resultado[i] = LetterState.PRESENT
                restantes[p[i]] = qtd - 1
            }
        }
        return resultado
    }
}
