package com.example.pfinal_pokezona_gabriel_jorge.ui.screens.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pfinal_pokezona_gabriel_jorge.ui.screens.main.tabs.OfficialGamesScreen
import com.example.pfinal_pokezona_gabriel_jorge.ui.screens.main.tabs.PokedexScreen
import com.example.pfinal_pokezona_gabriel_jorge.ui.screens.main.tabs.ProfileScreen

sealed class BottomNavItem(
        val route: String,
        val title: String,
        val icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    object Games : BottomNavItem("games", "Juegos", Icons.Default.PlayArrow)
    object Pokedex : BottomNavItem("pokedex", "Pokédex", Icons.Default.List)
    object Profile : BottomNavItem("profile", "Perfil", Icons.Default.Person)
}

@Composable
fun MainScreen(
        onGameClick: (String) -> Unit,
        onPokemonClick: (String) -> Unit,
        onLogoutClick: () -> Unit
) {
    val navController = rememberNavController()
    val items = listOf(BottomNavItem.Games, BottomNavItem.Pokedex, BottomNavItem.Profile)

    Scaffold(
            bottomBar = {
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    items.forEach { item ->
                        NavigationBarItem(
                                icon = { Icon(item.icon, contentDescription = item.title) },
                                label = { Text(item.title) },
                                selected =
                                        currentDestination?.hierarchy?.any {
                                            it.route == item.route
                                        } == true,
                                onClick = {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                        )
                    }
                }
            }
    ) { innerPadding ->
        NavHost(
                navController,
                startDestination = BottomNavItem.Games.route,
                Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Games.route) { OfficialGamesScreen(onGameClick = onGameClick) }
            composable(BottomNavItem.Pokedex.route) {
                PokedexScreen(onPokemonClick = onPokemonClick)
            }
            composable(BottomNavItem.Profile.route) { ProfileScreen(onLogout = onLogoutClick) }
        }
    }
}
