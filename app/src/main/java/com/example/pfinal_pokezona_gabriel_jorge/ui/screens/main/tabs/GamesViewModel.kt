//Gabriel Almarcha Martínez y Jorge Maqueda Miguel

package com.example.pfinal_pokezona_gabriel_jorge.ui.screens.main.tabs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pfinal_pokezona_gabriel_jorge.data.model.GameResult
import com.example.pfinal_pokezona_gabriel_jorge.data.network.RetrofitInstance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GamesViewModel : ViewModel() {

    private val _games = MutableStateFlow<List<GameResult>>(emptyList())
    val games: StateFlow<List<GameResult>> = _games.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _completedGamesIds = MutableStateFlow<Set<String>>(emptySet())
    val completedGamesIds: StateFlow<Set<String>> = _completedGamesIds.asStateFlow()

    // Lista de los juegos de la saga principal
    private val mainSeriesGames =
        listOf(
            "red",
            "blue",
            "yellow",
            "gold",
            "silver",
            "crystal",
            "ruby",
            "sapphire",
            "emerald",
            "firered",
            "leafgreen",
            "diamond",
            "pearl",
            "platinum",
            "heartgold",
            "soulsilver",
            "black",
            "white",
            "black-2",
            "white-2",
            "x",
            "y",
            "omega-ruby",
            "alpha-sapphire",
            "sun",
            "moon",
            "ultra-sun",
            "ultra-moon",
            "lets-go-pikachu",
            "lets-go-eevee",
            "sword",
            "shield",
            "brilliant-diamond",
            "shining-pearl",
            "legends-arceus",
            "scarlet",
            "violet",
            "legends-z-a"
        )

    init {
        fetchGames()
        fetchCompletedGames()
    }

    private fun fetchGames() {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val response = RetrofitInstance.api.getGames(limit = 100)

                // Filtramos los resultados para quedarnos con los de nuestra lista
                val filteredList =
                    response.results.filter { game -> mainSeriesGames.contains(game.name) }

                _games.value = filteredList
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun fetchCompletedGames() {
        val userId = auth.currentUser?.uid ?: return

        mainSeriesGames.forEach { gameId ->
            db.collection("users")
                .document(userId)
                .collection("games")
                .document(gameId)
                .collection("teams")
                .limit(1)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) return@addSnapshotListener

                    val currentSet = _completedGamesIds.value.toMutableSet()
                    if (snapshot != null && !snapshot.isEmpty) {
                        currentSet.add(gameId)
                    } else {
                        currentSet.remove(gameId)
                    }
                    _completedGamesIds.value = currentSet
                }
        }
    }
}
