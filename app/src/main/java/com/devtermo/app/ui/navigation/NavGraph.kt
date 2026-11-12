package com.devtermo.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.devtermo.app.model.Categorias
import com.devtermo.app.model.Dificuldades
import com.devtermo.app.model.Perfil
import com.devtermo.app.ui.screens.AdminScreen
import com.devtermo.app.ui.screens.GameScreen
import com.devtermo.app.ui.screens.HistoryScreen
import com.devtermo.app.ui.screens.HomeScreen
import com.devtermo.app.ui.screens.ProfileScreen
import com.devtermo.app.ui.screens.ProfileSelectScreen
import com.devtermo.app.ui.screens.RankingScreen
import com.devtermo.app.ui.screens.StatisticsScreen
import com.devtermo.app.ui.screens.WordFormScreen
import com.devtermo.app.ui.screens.WordListScreen
import com.devtermo.app.viewmodel.AdminViewModel
import com.devtermo.app.viewmodel.GameViewModel
import com.devtermo.app.viewmodel.ProfileViewModel
import com.devtermo.app.viewmodel.ViewModelFactory

/**
 * Responsavel: Integrante 1.
 * Controla a navegacao global e o acesso por perfil. Os ViewModels sao criados aqui (escopo da Activity)
 * e as telas recebem apenas estado e lambdas.
 */
@Composable
fun DevTermoNavHost(factory: ViewModelFactory) {
    val navController = rememberNavController()
    val profileVm: ProfileViewModel = viewModel(factory = factory)
    val gameVm: GameViewModel = viewModel(factory = factory)
    val adminVm: AdminViewModel = viewModel(factory = factory)

    val profile by profileVm.uiState.collectAsStateWithLifecycle()
    val game by gameVm.uiState.collectAsStateWithLifecycle()
    val admin by adminVm.uiState.collectAsStateWithLifecycle()

    val backStack by navController.currentBackStackEntryAsState()
    val rotaAtual = backStack?.destination?.route

    // Entrada e saida de perfil: leva o usuario para a area correta.
    LaunchedEffect(profile.perfil) {
        when (profile.perfil) {
            Perfil.JOGADOR -> navController.navigate(Routes.HOME) {
                popUpTo(navController.graph.id) { inclusive = true }
            }
            Perfil.ADMIN -> navController.navigate(Routes.ADMIN) {
                popUpTo(navController.graph.id) { inclusive = true }
            }
            null -> if (navController.currentDestination?.route != Routes.PERFIL) {
                navController.navigate(Routes.PERFIL) { popUpTo(navController.graph.id) { inclusive = true } }
            }
        }
    }

    // Protecao de rotas: jogador nao abre destinos administrativos.
    LaunchedEffect(rotaAtual) {
        val perfil = profile.perfil
        val bloqueada = rotaAtual != null && rotaAtual != Routes.PERFIL && (
            perfil == null ||
                (perfil == Perfil.JOGADOR && rotaAtual in Routes.somenteAdmin) ||
                (perfil == Perfil.ADMIN && rotaAtual in Routes.somenteJogador)
            )
        if (bloqueada) {
            navController.navigate(Routes.PERFIL) { popUpTo(navController.graph.id) { inclusive = true } }
        }
    }

    val itens = itensDaBarra(profile.perfil)
    val mostrarBarra = itens.any { it.route == rotaAtual }

    Scaffold(
        bottomBar = {
            if (mostrarBarra) {
                DevTermoBottomBar(itens = itens, rotaAtual = rotaAtual) { item ->
                    navController.navegarPelaBarra(item.route, itens.first().route)
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Routes.PERFIL,
            modifier = Modifier.padding(padding)
        ) {
            composable(Routes.PERFIL) {
                ProfileSelectScreen(
                    onJogador = { profileVm.entrar(Perfil.JOGADOR) },
                    onAdmin = { profileVm.entrar(Perfil.ADMIN, "Administrador") }
                )
            }
            composable(Routes.HOME) {
                HomeScreen(
                    state = profile,
                    onIniciar = {
                        // Selecao de categoria e dificuldade: Integrante 2 (por enquanto valores padrao).
                        gameVm.iniciarPartida(Categorias.LINGUAGENS, Dificuldades.FACIL)
                        navController.navigate(Routes.GAME)
                    },
                    onRanking = { navController.navegarPelaBarra(Routes.RANKING, Routes.HOME) }
                )
            }
            composable(Routes.GAME) {
                GameScreen(
                    state = game,
                    onLetra = gameVm::onLetra,
                    onApagar = gameVm::onApagar,
                    onConfirmar = gameVm::confirmarTentativa,
                    onSair = {
                        gameVm.limparMensagens()
                        navController.popBackStack()
                    }
                )
            }
            composable(Routes.RANKING) { RankingScreen(state = profile) }
            composable(Routes.HISTORY) { HistoryScreen(state = profile) }
            composable(Routes.PROFILE) { ProfileScreen(state = profile, onSair = profileVm::sair) }
            composable(Routes.ADMIN) {
                AdminScreen(
                    state = admin,
                    geral = profile,
                    onGerenciar = { navController.navegarPelaBarra(Routes.WORDS, Routes.ADMIN) },
                    onSincronizar = adminVm::sincronizar,
                    onLimparMensagens = adminVm::limparMensagens
                )
            }
            composable(Routes.WORDS) {
                WordListScreen(
                    state = admin,
                    onBusca = adminVm::onBuscaChange,
                    onCategoria = adminVm::onCategoriaChange,
                    onDificuldade = adminVm::onDificuldadeChange,
                    onAdicionar = {
                        adminVm.limparMensagens()
                        adminVm.iniciarEdicao(null)
                        navController.navigate(Routes.wordForm(null))
                    },
                    onEditar = { id ->
                        adminVm.limparMensagens()
                        adminVm.iniciarEdicao(id)
                        navController.navigate(Routes.wordForm(id))
                    },
                    onExcluir = adminVm::excluirPalavra,
                    onLimparMensagens = adminVm::limparMensagens
                )
            }
            composable(
                route = Routes.WORD_FORM,
                arguments = listOf(
                    navArgument(Routes.ARG_WORD_ID) { type = NavType.IntType; defaultValue = -1 }
                )
            ) {
                // Fecha o formulario quando o salvamento termina com sucesso.
                LaunchedEffect(admin.mensagemSucesso) {
                    if (admin.mensagemSucesso != null) navController.popBackStack()
                }
                WordFormScreen(
                    state = admin,
                    onSalvar = adminVm::salvarPalavra,
                    onCancelar = { navController.popBackStack() }
                )
            }
            composable(Routes.STATS) { StatisticsScreen(state = profile) }
        }
    }
}

private fun NavHostController.navegarPelaBarra(rota: String, primeiraRota: String) {
    navigate(rota) {
        popUpTo(primeiraRota) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
