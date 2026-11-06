package com.devtermo.app.data.repository

import com.devtermo.app.model.Perfil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Responsavel: Integrante 1 (com Integrante 6 no ProfileViewModel).
 * Sessao em memoria: perfil ativo e nome do jogador. Controle de acesso DEMONSTRATIVO
 * (sem autenticacao real). Registrar essa limitacao na documentacao.
 */
class SessionRepository {
    private val _perfil = MutableStateFlow<Perfil?>(null)
    val perfil: StateFlow<Perfil?> = _perfil.asStateFlow()

    private val _jogador = MutableStateFlow("Jogador")
    val jogador: StateFlow<String> = _jogador.asStateFlow()

    fun entrar(perfil: Perfil, nomeJogador: String = "Jogador") {
        _perfil.value = perfil
        _jogador.value = nomeJogador.ifBlank { "Jogador" }
    }

    fun sair() {
        _perfil.value = null
    }

    fun isAdmin(): Boolean = _perfil.value == Perfil.ADMIN
}
