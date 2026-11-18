package com.devtermo.app.ui.navigation

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

    val somenteAdmin = setOf(ADMIN, WORDS, STATS, WORD_FORM)

    val somenteJogador = setOf(HOME, GAME, RANKING, HISTORY)
}
