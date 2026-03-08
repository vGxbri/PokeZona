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

    private val _pokemonDescription = MutableStateFlow<String>("")
    val pokemonDescription: StateFlow<String> = _pokemonDescription.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Función que llamaremos desde la pantalla pasándole el ID o nombre
    fun fetchPokemonDetail(pokemonName: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // Convertimos el nombre a minúsculas porque la API es estricta con eso
                val lowerName = pokemonName.lowercase()
                val response = RetrofitInstance.api.getPokemonDetail(lowerName)
                _pokemonDetail.value = response

                val speciesResponse = RetrofitInstance.api.getPokemonSpecies(lowerName)
                val spanishEntry =
                        speciesResponse.flavorTextEntries.firstOrNull { it.language.name == "es" }
                val cleanText =
                        spanishEntry?.flavorText?.replace(Regex("""[\n\r\f]"""), " ")
                                ?: "Descripción no disponible en español."
                _pokemonDescription.value = cleanText
            } catch (e: Exception) {
                e.printStackTrace()
                _pokemonDescription.value = "Error al cargar la descripción."
            } finally {
                _isLoading.value = false
            }
        }
    }
}
