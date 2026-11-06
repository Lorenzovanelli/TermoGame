package com.devtermo.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.devtermo.app.data.repository.MatchRepository
import com.devtermo.app.data.repository.SessionRepository
import com.devtermo.app.data.repository.WordRepository

/**
 * Responsavel: Integrante 1.
 * Injeta os repositories nos ViewModels. Ao criar um novo ViewModel, adicionar um ramo no when.
 */
class ViewModelFactory(
    private val wordRepository: WordRepository,
    private val matchRepository: MatchRepository,
    private val sessionRepository: SessionRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(GameViewModel::class.java) ->
            GameViewModel(wordRepository, matchRepository, sessionRepository) as T
        modelClass.isAssignableFrom(AdminViewModel::class.java) ->
            AdminViewModel(wordRepository, sessionRepository) as T
        modelClass.isAssignableFrom(ProfileViewModel::class.java) ->
            ProfileViewModel(matchRepository, sessionRepository) as T
        else -> throw IllegalArgumentException("ViewModel desconhecido: ${modelClass.name}")
    }
}
