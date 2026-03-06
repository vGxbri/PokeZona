package com.example.pfinal_pokezona_gabriel_jorge.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pfinal_pokezona_gabriel_jorge.ui.screens.auth.AuthState
import com.example.pfinal_pokezona_gabriel_jorge.ui.screens.auth.AuthViewModel
import com.example.pfinal_pokezona_gabriel_jorge.ui.screens.auth.LoginScreen
import com.example.pfinal_pokezona_gabriel_jorge.ui.screens.auth.RegisterScreen
import com.example.pfinal_pokezona_gabriel_jorge.ui.screens.details.GameDetailScreen
import com.example.pfinal_pokezona_gabriel_jorge.ui.screens.details.PokemonDetailScreen
import com.example.pfinal_pokezona_gabriel_jorge.ui.screens.main.MainScreen
import com.example.pfinal_pokezona_gabriel_jorge.ui.screens.details.CreateTeamScreen

@Composable
fun AppNavigation(authViewModel: AuthViewModel = viewModel()) {
    val navController = rememberNavController()
    val authState by authViewModel.authState.collectAsState()

    // Redirigir al Login si la sesión se cierra (authState pasa a Idle)
    LaunchedEffect(authState) {
        if (authState is AuthState.Idle) {
            navController.navigate("login") { popUpTo(0) { inclusive = true } }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (authState is AuthState.Success) "main" else "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("main") { popUpTo("login") { inclusive = true } }
                },
                onNavigateToRegister = { navController.navigate("register") },
                viewModel = authViewModel
            )
        }
        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate("main") { popUpTo("login") { inclusive = true } }
                },
                onNavigateBack = { navController.popBackStack() },
                viewModel = authViewModel
            )
        }
        composable("main") {
            MainScreen(
                // Llama a "gameDetail"
                onGameClick = { gameId -> navController.navigate("gameDetail/$gameId") },
                onPokemonClick = { pokemonId ->
                    navController.navigate("pokemonDetail/$pokemonId")
                },
                onLogoutClick = { authViewModel.signOut() }
            )
        }

        composable("gameDetail/{gameId}") { backStackEntry ->
            val gameId = backStackEntry.arguments?.getString("gameId") ?: return@composable
            GameDetailScreen(
                gameId = gameId,
                onBackClick = { navController.popBackStack() },
                onAddTeamClick = { id -> navController.navigate("create_team/$id") }
            )
        }

        composable("create_team/{gameId}") { backStackEntry ->
            val gameId = backStackEntry.arguments?.getString("gameId") ?: return@composable
            CreateTeamScreen(
                gameId = gameId,
                onBackClick = { navController.popBackStack() }
            )
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