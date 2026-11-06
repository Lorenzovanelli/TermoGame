package com.devtermo.app.viewmodel

import com.devtermo.app.model.Palavra

/** Responsavel: Integrante 5. Estado da area administrativa de palavras. */
data class AdminUiState(
    val palavras: List<Palavra> = emptyList(),
    val totalPalavras: Int = 0,
    val categoriasDisponiveis: List<String> = emptyList(),
    val busca: String = "",
    val filtroCategoria: String? = null,
    val filtroDificuldade: String? = null,
    val palavraEmEdicao: Palavra? = null,
    val isLoading: Boolean = false,
    val erro: String? = null,
    val mensagemSucesso: String? = null
)
