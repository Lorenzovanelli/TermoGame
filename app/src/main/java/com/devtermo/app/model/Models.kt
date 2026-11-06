package com.devtermo.app.model

/** Perfis de acesso do app. */
enum class Perfil { JOGADOR, ADMIN }

/** Modelo de dominio de uma palavra do catalogo. Usado pela UI e pelos ViewModels. */
data class Palavra(
    val id: Int = 0,
    val palavra: String,
    val categoria: String,
    val dificuldade: String,
    val dica: String,
    val remoteId: String? = null,
    val sincronizado: Boolean = false
)

/** Modelo de dominio do historico de partidas. */
data class Partida(
    val id: Int = 0,
    val palavraId: Int,
    val jogador: String,
    val resultado: String,
    val tentativas: Int,
    val pontuacao: Int,
    val data: Long
)

/** Linha do ranking (calculada a partir das partidas). */
data class RankingItem(
    val posicao: Int,
    val jogador: String,
    val pontuacao: Int,
    val vitorias: Int
)

object Dificuldades {
    const val FACIL = "Fácil"
    const val MEDIO = "Médio"
    const val DIFICIL = "Difícil"
    val todas = listOf(FACIL, MEDIO, DIFICIL)
}

object Categorias {
    const val LINGUAGENS = "Linguagens de programação"
    const val CONCEITOS = "Conceitos de programação"
    const val MOBILE = "Desenvolvimento mobile"
    const val WEB = "Desenvolvimento web"
    const val BANCO_DADOS = "Banco de dados"
    const val FRAMEWORKS = "Frameworks e bibliotecas"
    val todas = listOf(LINGUAGENS, CONCEITOS, MOBILE, WEB, BANCO_DADOS, FRAMEWORKS)
}

object Resultados {
    const val VITORIA = "VITORIA"
    const val DERROTA = "DERROTA"
}
