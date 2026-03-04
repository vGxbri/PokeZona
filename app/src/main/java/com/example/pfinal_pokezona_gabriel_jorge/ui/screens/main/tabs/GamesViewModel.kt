package com.example.pfinal_pokezona_gabriel_jorge.ui.screens.main.tabs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pfinal_pokezona_gabriel_jorge.data.model.GameResult
import com.example.pfinal_pokezona_gabriel_jorge.data.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GamesViewModel : ViewModel() {

    private val _games = MutableStateFlow<List<GameResult>>(emptyList())
    val games: StateFlow<List<GameResult>> = _games.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Lista exacta de las versiones de la saga principal (como las llama la PokéAPI)
    private val mainSeriesGames = listOf(
        "red", "blue", "yellow",
        "gold", "silver", "crystal",
        "ruby", "sapphire", "emerald", "firered", "leafgreen",
        "diamond", "pearl", "platinum", "heartgold", "soulsilver",
        "black", "white", "black-2", "white-2",
        "x", "y", "omega-ruby", "alpha-sapphire",
        "sun", "moon", "ultra-sun", "ultra-moon",
        "lets-go-pikachu", "lets-go-eevee",
        "sword", "shield", "brilliant-diamond", "shining-pearl", "legends-arceus",
        "scarlet", "violet", "legends-z-a"
    )

    init {
        fetchGames()
    }

    private fun fetchGames() {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                // Pedimos 100 juegos para asegurarnos de que llegamos hasta Escarlata y Púrpura
                val response = RetrofitInstance.api.getGames(limit = 100)

                // Filtramos los resultados: solo nos quedamos con los que estén en nuestra lista
                val filteredList = response.results.filter { game ->
                    mainSeriesGames.contains(game.name)
                }

                _games.value = filteredList
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}