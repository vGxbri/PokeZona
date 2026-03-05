package com.example.pfinal_pokezona_gabriel_jorge.ui.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pfinal_pokezona_gabriel_jorge.data.model.PokemonDetailResponse
import com.example.pfinal_pokezona_gabriel_jorge.data.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PokemonDetailViewModel : ViewModel() {

    // Guardará toda la ficha del Pokémon
    private val _pokemonDetail = MutableStateFlow<PokemonDetailResponse?>(null)
    val pokemonDetail: StateFlow<PokemonDetailResponse?> = _pokemonDetail.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Función que llamaremos desde la pantalla pasándole el ID o nombre
    fun fetchPokemonDetail(pokemonName: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // Convertimos el nombre a minúsculas porque la API es estricta con eso
                val response = RetrofitInstance.api.getPokemonDetail(pokemonName.lowercase())
                _pokemonDetail.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}