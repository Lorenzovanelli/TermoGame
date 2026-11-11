package com.devtermo.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devtermo.app.data.repository.AppResult
import com.devtermo.app.data.repository.SessionRepository
import com.devtermo.app.data.repository.WordRepository
import com.devtermo.app.logic.WordValidator
import com.devtermo.app.model.Palavra
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdminViewModel(
    private val wordRepository: WordRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private data class Filtros(val busca: String = "", val categoria: String? = null, val dificuldade: String? = null)

    private val filtros = MutableStateFlow(Filtros())
    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(wordRepository.palavras, filtros) { todas, f -> Pair(todas, f) }
                .catch { e -> _uiState.update { it.copy(erro = "Erro ao carregar palavras: ${e.message}") } }
                .collect { (todas, f) ->
                    val categorias = todas.map { it.categoria }.distinct().sorted()
                    val categoria = f.categoria?.takeIf { it in categorias }
                    val filtradas = todas
                        .filter { categoria == null || it.categoria == categoria }
                        .filter { f.dificuldade == null || it.dificuldade == f.dificuldade }
                        .filter { f.busca.isBlank() || it.palavra.contains(f.busca, ignoreCase = true) }
                        .sortedBy { it.palavra }
                    _uiState.update {
                        it.copy(
                            palavras = filtradas,
                            totalPalavras = todas.size,
                            categoriasDisponiveis = categorias,
                            busca = f.busca,
                            filtroCategoria = categoria,
                            filtroDificuldade = f.dificuldade
                        )
                    }
                }
        }
    }

    fun onBuscaChange(texto: String) = filtros.update { it.copy(busca = texto) }
    fun onCategoriaChange(categoria: String?) = filtros.update { it.copy(categoria = categoria) }
    fun onDificuldadeChange(dificuldade: String?) = filtros.update { it.copy(dificuldade = dificuldade) }

    fun iniciarEdicao(id: Int?) {
        if (id == null) {
            _uiState.update { it.copy(palavraEmEdicao = null) }
            return
        }
        viewModelScope.launch {
            when (val r = wordRepository.buscarPorId(id)) {
                is AppResult.Success -> _uiState.update { it.copy(palavraEmEdicao = r.data) }
                is AppResult.Error -> _uiState.update { it.copy(erro = r.mensagem) }
            }
        }
    }

    fun salvarPalavra(palavra: Palavra) {
        if (!permitirEscrita()) return
        val texto = WordValidator.normalizar(palavra.palavra)
        if (texto.length < 3 || !texto.all { it.isLetter() }) {
            _uiState.update { it.copy(erro = "Informe uma palavra com ao menos 3 letras, sem espaços ou números.") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, erro = null) }
            when (val r = wordRepository.salvar(palavra.copy(palavra = texto))) {
                is AppResult.Success -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        palavraEmEdicao = null,
                        mensagemSucesso = if (r.data) "Palavra salva e sincronizada."
                        else "Palavra salva no aparelho. Sincronização pendente."
                    )
                }
                is AppResult.Error -> _uiState.update { it.copy(isLoading = false, erro = r.mensagem) }
            }
        }
    }

    fun excluirPalavra(id: Int) {
        if (!permitirEscrita()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, erro = null) }
            when (val r = wordRepository.excluir(id)) {
                is AppResult.Success -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        mensagemSucesso = if (r.data) "Palavra excluída."
                        else "Palavra excluída no aparelho. A nuvem ainda não foi atualizada."
                    )
                }
                is AppResult.Error -> _uiState.update { it.copy(isLoading = false, erro = r.mensagem) }
            }
        }
    }

    fun sincronizar() {
        if (!permitirEscrita()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, erro = null) }
            val recebidas = when (val r = wordRepository.importarCatalogoRemoto()) {
                is AppResult.Success -> r.data
                is AppResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, erro = r.mensagem) }
                    return@launch
                }
            }
            when (val r = wordRepository.sincronizarPendentes()) {
                is AppResult.Success -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        mensagemSucesso = "Sincronização concluída: $recebidas recebida(s), ${r.data} enviada(s)."
                    )
                }
                is AppResult.Error -> _uiState.update { it.copy(isLoading = false, erro = r.mensagem) }
            }
        }
    }

    fun limparMensagens() {
        _uiState.update { it.copy(erro = null, mensagemSucesso = null) }
    }

    private fun permitirEscrita(): Boolean {
        val ok = sessionRepository.isAdmin()
        if (!ok) _uiState.update { it.copy(erro = "Acesso restrito ao administrador.") }
        return ok
    }
}
