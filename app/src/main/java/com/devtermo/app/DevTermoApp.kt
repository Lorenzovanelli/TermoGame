package com.devtermo.app

import android.app.Application
import com.devtermo.app.data.local.AppDatabase
import com.devtermo.app.data.remote.FirebaseDataSource
import com.devtermo.app.data.repository.MatchRepository
import com.devtermo.app.data.repository.SessionRepository
import com.devtermo.app.data.repository.WordRepository
import com.devtermo.app.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Responsavel: Integrante 1.
 * Cria uma unica vez banco, fonte remota e repositories, e entrega a ViewModelFactory.
 */
class DevTermoApp : Application() {

    private val database by lazy { AppDatabase.getInstance(this) }
    private val remote by lazy { FirebaseDataSource() }

    private val wordRepository by lazy { WordRepository(database.palavraDao(), remote) }
    private val matchRepository by lazy { MatchRepository(database.partidaDao()) }
    private val sessionRepository by lazy { SessionRepository() }

    val viewModelFactory by lazy { ViewModelFactory(wordRepository, matchRepository, sessionRepository) }

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch { wordRepository.semearSeVazio() }
    }
}
