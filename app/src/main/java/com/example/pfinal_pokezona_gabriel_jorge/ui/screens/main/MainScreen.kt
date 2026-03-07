package com.example.pfinal_pokezona_gabriel_jorge.ui.screens.main

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pfinal_pokezona_gabriel_jorge.R
import com.example.pfinal_pokezona_gabriel_jorge.ui.screens.main.tabs.OfficialGamesScreen
import com.example.pfinal_pokezona_gabriel_jorge.ui.screens.main.tabs.PokedexScreen
import com.example.pfinal_pokezona_gabriel_jorge.ui.screens.main.tabs.ProfileScreen

sealed class BottomNavItem(
        val route: String,
        val title: String,
        @DrawableRes val icon: Int,
        @DrawableRes val selectedIcon: Int
) {
        object Games :
                BottomNavItem(
                        "games",
                        "Juegos",
                        R.drawable.game_controller,
                        R.drawable.game_controller_fill
                )
        object Pokedex :
                BottomNavItem("pokedex", "Pokédex", R.drawable.pokeball, R.drawable.pokeball_fill)
        object Profile : BottomNavItem("profile", "Perfil", R.drawable.user, R.drawable.user_fill)
}

@Composable
fun MainScreen(
        onGameClick: (String) -> Unit,
        onPokemonClick: (String) -> Unit,
        onLogoutClick: () -> Unit
) {
        val navController = rememberNavController()
        val items = listOf(BottomNavItem.Games, BottomNavItem.Pokedex, BottomNavItem.Profile)

        // Ya no usamos bottomBar aquí, lo metemos todo en el Box
        Scaffold { innerPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {

                        // CAPA 1: Las pantallas (NavHost)
                        NavHost(
                                navController = navController,
                                startDestination = BottomNavItem.Games.route,
                                modifier = Modifier.fillMaxSize()
                        ) {
                                composable(BottomNavItem.Games.route) {
                                        OfficialGamesScreen(onGameClick = onGameClick)
                                }
                                composable(BottomNavItem.Pokedex.route) {
                                        PokedexScreen(onPokemonClick = onPokemonClick)
                                }
                                composable(BottomNavItem.Profile.route) {
                                        ProfileScreen(onLogoutClick = onLogoutClick)
                                }
                        }

                        // CAPA 2: El degradado decorativo (Detrás de la barra)
                        Box(
                                modifier =
                                        Modifier.align(Alignment.BottomCenter)
                                                .fillMaxWidth()
                                                .height(
                                                        180.dp
                                                ) // Ajusta este valor si quieres más o menos efecto
                                                // niebla
                                                .background(
                                                        Brush.verticalGradient(
                                                                colors =
                                                                        listOf(
                                                                                Color.Transparent,
                                                                                MaterialTheme
                                                                                        .colorScheme
                                                                                        .background // Se funde
                                                                                // con el
                                                                                // fondo de
                                                                                // la app
                                                                                )
                                                        )
                                                )
                        )

                        // CAPA 3: La barra de navegación flotante
                        Surface(
                                modifier =
                                        Modifier.align(
                                                        Alignment.BottomCenter
                                                ) // Se pega abajo del Box
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
                                        modifier =
                                                Modifier.height(64.dp).padding(horizontal = 8.dp),
                                        containerColor = Color.Transparent,
                                        tonalElevation = 0.dp,
                                        windowInsets = WindowInsets(0.dp)
                                ) {
                                        val navBackStackEntry by
                                                navController.currentBackStackEntryAsState()
                                        val currentDestination = navBackStackEntry?.destination

                                        items.forEach { item ->
                                                val selected =
                                                        currentDestination?.hierarchy?.any {
                                                                it.route == item.route
                                                        } == true

                                                val pillWidth by
                                                        animateDpAsState(
                                                                targetValue =
                                                                        if (selected) 64.dp
                                                                        else 32.dp,
                                                                label = "pillWidth"
                                                        )
                                                val pillColor by
                                                        animateColorAsState(
                                                                targetValue =
                                                                        if (selected)
                                                                                MaterialTheme
                                                                                        .colorScheme
                                                                                        .primary
                                                                        else Color.Transparent,
                                                                label = "pillColor"
                                                        )

                                                NavigationBarItem(
                                                        selected = selected,
                                                        onClick = {
                                                                navController.navigate(item.route) {
                                                                        popUpTo(
                                                                                navController.graph
                                                                                        .findStartDestination()
                                                                                        .id
                                                                        ) { saveState = true }
                                                                        launchSingleTop = true
                                                                        restoreState = true
                                                                }
                                                        },
                                                        colors =
                                                                NavigationBarItemDefaults.colors(
                                                                        indicatorColor =
                                                                                Color.Transparent,
                                                                        selectedIconColor =
                                                                                MaterialTheme
                                                                                        .colorScheme
                                                                                        .background,
                                                                        unselectedIconColor =
                                                                                MaterialTheme
                                                                                        .colorScheme
                                                                                        .onBackground
                                                                                        .copy(
                                                                                                alpha =
                                                                                                        0.5f
                                                                                        )
                                                                ),
                                                        icon = {
                                                                Box(
                                                                        contentAlignment =
                                                                                Alignment.Center,
                                                                        modifier =
                                                                                Modifier.height(
                                                                                                40.dp
                                                                                        )
                                                                                        .width(
                                                                                                pillWidth
                                                                                        )
                                                                                        .background(
                                                                                                color =
                                                                                                        pillColor,
                                                                                                shape =
                                                                                                        RoundedCornerShape(
                                                                                                                20.dp
                                                                                                        )
                                                                                        )
                                                                ) {
                                                                        Icon(
                                                                                modifier =
                                                                                        Modifier.size(
                                                                                                32.dp
                                                                                        ),
                                                                                painter =
                                                                                        painterResource(
                                                                                                id =
                                                                                                        if (selected
                                                                                                        )
                                                                                                                item.selectedIcon
                                                                                                        else
                                                                                                                item.icon
                                                                                        ),
                                                                                contentDescription =
                                                                                        item.title
                                                                        )
                                                                }
                                                        }
                                                )
                                        }
                                }
                        }
                }
        }
}
