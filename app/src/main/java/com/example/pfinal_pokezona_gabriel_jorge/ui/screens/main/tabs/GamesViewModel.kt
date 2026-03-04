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

    // Aquí guardaremos la lista de juegos. Empieza vacía.
    private val _games = MutableStateFlow<List<GameResult>>(emptyList())
    val games: StateFlow<List<GameResult>> = _games.asStateFlow()

    // Para saber si está cargando y mostrar una ruedita de carga en la pantalla
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        // Nada más crearse el ViewModel, pedimos los juegos
        fetchGames()
    }

    private fun fetchGames() {
        // Usamos viewModelScope porque las peticiones a internet se hacen en segundo plano
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // Llamamos a nuestra API
                val response = RetrofitInstance.api.getGames()
                // Guardamos los resultados
                _games.value = response.results
            } catch (e: Exception) {
                // Si falla (ej. no hay internet), por ahora solo imprimimos el error
                e.printStackTrace()
            } finally {
                // Termina la carga
                _isLoading.value = false
            }
        }
    }
}