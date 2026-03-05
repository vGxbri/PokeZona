package com.example.pfinal_pokezona_gabriel_jorge.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pfinal_pokezona_gabriel_jorge.ui.screens.auth.LoginScreen
import com.example.pfinal_pokezona_gabriel_jorge.ui.screens.auth.RegisterScreen
import com.example.pfinal_pokezona_gabriel_jorge.ui.screens.details.GameDetailScreen
import com.example.pfinal_pokezona_gabriel_jorge.ui.screens.details.PokemonDetailScreen
import com.example.pfinal_pokezona_gabriel_jorge.ui.screens.main.MainScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                    onLoginSuccess = {
                        navController.navigate("main") { popUpTo("login") { inclusive = true } }
                    },
                    onNavigateToRegister = { navController.navigate("register") }
            )
        }
        composable("register") {
            RegisterScreen(
                    onRegisterSuccess = {
                        navController.navigate("main") { popUpTo("login") { inclusive = true } }
                    },
                    onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("main") {
            MainScreen(
                    onGameClick = { gameId -> navController.navigate("gameDetail/$gameId") },
                    onPokemonClick = { pokemonId ->
                        navController.navigate("pokemonDetail/$pokemonId")
                    },
                    onLogoutClick = {
                        navController.navigate("login") { popUpTo(0) { inclusive = true } }
                    }
            )
        }
        composable(
                route = "gameDetail/{gameId}",
                arguments = listOf(navArgument("gameId") { type = NavType.StringType })
        ) { backStackEntry ->
            val gameId = backStackEntry.arguments?.getString("gameId") ?: ""
            GameDetailScreen(gameId = gameId, onBackClick = { navController.popBackStack() })
        }
        composable(
                route = "pokemonDetail/{pokemonId}",
                arguments = listOf(navArgument("pokemonId") { type = NavType.StringType })
        ) { backStackEntry ->
            val pokemonId = backStackEntry.arguments?.getString("pokemonId") ?: ""
            PokemonDetailScreen(
                    pokemonId = pokemonId,
                    onBackClick = { navController.popBackStack() }
            )
        }
    }
}
