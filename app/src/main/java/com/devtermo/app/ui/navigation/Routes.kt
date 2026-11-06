package com.devtermo.app.ui.navigation

/** Responsavel: Integrante 1. Todas as rotas em um unico lugar, sem strings soltas. */
object Routes {
    const val PERFIL = "perfil"
    const val HOME = "home"
    const val GAME = "game"
    const val RANKING = "ranking"
    const val HISTORY = "history"
    const val PROFILE = "profile"
    const val ADMIN = "admin"
    const val WORDS = "words"
    const val STATS = "stats"

    const val ARG_WORD_ID = "id"
    const val WORD_FORM = "word_form?id={id}"

    fun wordForm(id: Int?): String = if (id == null) "word_form" else "word_form?id=$id"

    /** Destinos que um jogador nao pode abrir. */
    val somenteAdmin = setOf(ADMIN, WORDS, STATS, WORD_FORM)

    /** Destinos da area do jogador (nao exibidos ao administrador). */
    val somenteJogador = setOf(HOME, GAME, RANKING, HISTORY)
}
