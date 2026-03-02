package com.example.pfinal_pokezona_gabriel_jorge.ui.screens.main

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
            Surface(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 24.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(32.dp)
                    ),
                shape = RoundedCornerShape(28.dp),
                shadowElevation = 12.dp,
                tonalElevation = 0.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                NavigationBar(
                    modifier = Modifier
                        .height(64.dp)
                        .padding(horizontal = 8.dp),
                    containerColor = Color.Transparent,
                    tonalElevation = 0.dp,
                    windowInsets = WindowInsets(0.dp)
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    items.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any {
                            it.route == item.route
                        } == true

                        // Animaciones de estado
                        val pillWidth by animateDpAsState(
                            targetValue = if (selected) 64.dp else 32.dp,
                            label = "pillWidth"
                        )
                        val pillColor by animateColorAsState(
                            targetValue = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
                            label = "pillColor"
                        )

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color.Transparent,
                                selectedIconColor = MaterialTheme.colorScheme.background,
                                unselectedIconColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                            ),
                            icon = {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .height(40.dp)
                                        .width(pillWidth) // Usamos el valor animado
                                        .background(
                                            color = pillColor, // Usamos el color animado
                                            shape = RoundedCornerShape(20.dp)
                                        )
                                ) {
                                    Icon(
                                        modifier = Modifier.size(28.dp),
                                        imageVector = item.icon,
                                        contentDescription = item.title
                                    )
                                }
                            }
                        )
                    }
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