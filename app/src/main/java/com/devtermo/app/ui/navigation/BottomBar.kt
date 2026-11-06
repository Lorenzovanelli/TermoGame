package com.devtermo.app.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.devtermo.app.R
import com.devtermo.app.model.Perfil

data class BottomItem(val route: String, @StringRes val label: Int, val icon: ImageVector)

/** Responsavel: Integrante 1. Itens da barra inferior por perfil. */
fun itensDaBarra(perfil: Perfil?): List<BottomItem> = when (perfil) {
    Perfil.JOGADOR -> listOf(
        BottomItem(Routes.HOME, R.string.nav_inicio, Icons.Filled.Home),
        BottomItem(Routes.RANKING, R.string.nav_ranking, Icons.Filled.Star),
        BottomItem(Routes.HISTORY, R.string.nav_historico, Icons.Filled.Info),
        BottomItem(Routes.PROFILE, R.string.nav_perfil, Icons.Filled.Person)
    )
    Perfil.ADMIN -> listOf(
        BottomItem(Routes.ADMIN, R.string.nav_painel, Icons.Filled.Settings),
        BottomItem(Routes.WORDS, R.string.nav_palavras, Icons.Filled.Edit),
        BottomItem(Routes.STATS, R.string.nav_estatisticas, Icons.Filled.Info),
        BottomItem(Routes.PROFILE, R.string.nav_perfil, Icons.Filled.Person)
    )
    null -> emptyList()
}

@Composable
fun DevTermoBottomBar(
    itens: List<BottomItem>,
    rotaAtual: String?,
    onNavegar: (BottomItem) -> Unit
) {
    NavigationBar {
        itens.forEach { item -> ItemDaBarra(item, item.route == rotaAtual, onNavegar) }
    }
}

@Composable
private fun RowScope.ItemDaBarra(item: BottomItem, selecionado: Boolean, onNavegar: (BottomItem) -> Unit) {
    NavigationBarItem(
        selected = selecionado,
        onClick = { onNavegar(item) },
        icon = { Icon(item.icon, contentDescription = null) },
        label = { Text(stringResource(item.label)) }
    )
}
