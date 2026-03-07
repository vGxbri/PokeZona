package com.example.pfinal_pokezona_gabriel_jorge.ui.screens.main.tabs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pfinal_pokezona_gabriel_jorge.data.model.PokemonResult
import com.example.pfinal_pokezona_gabriel_jorge.data.network.RetrofitInstance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PokedexViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _pokemons = MutableStateFlow<List<PokemonResult>>(emptyList())
    val pokemons: StateFlow<List<PokemonResult>> = _pokemons.asStateFlow()

    private val _filteredPokemons = MutableStateFlow<List<PokemonResult>>(emptyList())
    val filteredPokemons: StateFlow<List<PokemonResult>> = _filteredPokemons.asStateFlow()

    private val _selectedGeneration = MutableStateFlow<String?>(null)
    val selectedGeneration: StateFlow<String?> = _selectedGeneration.asStateFlow()

    private val _selectedType = MutableStateFlow<String?>(null)
    val selectedType: StateFlow<String?> = _selectedType.asStateFlow()

    val availableGenerations =
            listOf("Gen 1", "Gen 2", "Gen 3", "Gen 4", "Gen 5", "Gen 6", "Gen 7", "Gen 8", "Gen 9")
    val availableTypes =
            listOf(
                    "normal",
                    "fire",
                    "water",
                    "grass",
                    "electric",
                    "ice",
                    "fighting",
                    "poison",
                    "ground",
                    "flying",
                    "psychic",
                    "bug",
                    "rock",
                    "ghost",
                    "dragon",
                    "dark",
                    "steel",
                    "fairy"
            )

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // NUEVO: Estado para guardar la lista de favoritos del usuario actual
    private val _favoritePokemons = MutableStateFlow<Set<String>>(emptySet())
    val favoritePokemons: StateFlow<Set<String>> = _favoritePokemons.asStateFlow()

    init {
        fetchPokemons()
        listenToFavorites() // Arrancamos la escucha de Firebase
    }

    private fun listenToFavorites() {
        val userId = auth.currentUser?.uid ?: return

        // Escuchamos el documento del usuario en tiempo real
        db.collection("users").document(userId).addSnapshotListener { snapshot, error ->
            if (error != null) return@addSnapshotListener
            if (snapshot != null && snapshot.exists()) {
                // Extraemos la lista de favoritos de Firestore
                val favs = snapshot.get("favorites") as? List<String> ?: emptyList()
                _favoritePokemons.value = favs.toSet()
            }
        }
    }

    fun toggleFavorite(pokemonName: String) {
        val userId = auth.currentUser?.uid ?: return
        val currentFavs = _favoritePokemons.value.toMutableSet()

        // Lógica: Si ya está, lo quitamos. Si no está, lo añadimos.
        if (currentFavs.contains(pokemonName)) {
            currentFavs.remove(pokemonName)
        } else {
            currentFavs.add(pokemonName)
        }

        // 1. Actualización optimista: Cambiamos la UI al instante para que se sienta súper rápida
        _favoritePokemons.value = currentFavs

        // 2. Guardamos en Firebase en segundo plano
        // Usamos SetOptions.merge() para que cree el documento si el usuario es nuevo y aún no
        // tiene uno
        db.collection("users")
                .document(userId)
                .set(hashMapOf("favorites" to currentFavs.toList()), SetOptions.merge())
    }

    private fun fetchPokemons() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = RetrofitInstance.api.getPokemons(limit = 1500)
                _pokemons.value = response.results
                applyFilters() // Apply any initial filters
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun applyFilters() {
        viewModelScope.launch {
            _isLoading.value = true
            var currentList = _pokemons.value

            // Filtrar por generación
            _selectedGeneration.value?.let { gen ->
                val range =
                        when (gen) {
                            "Gen 1" -> 1..151
                            "Gen 2" -> 152..251
                            "Gen 3" -> 252..386
                            "Gen 4" -> 387..493
                            "Gen 5" -> 494..649
                            "Gen 6" -> 650..721
                            "Gen 7" -> 722..809
                            "Gen 8" -> 810..905
                            "Gen 9" -> 906..1025
                            else -> 1..1500
                        }
                currentList =
                        currentList.filter {
                            val id = it.getPokemonId().toIntOrNull() ?: 0
                            id in range
                        }
            }

            // Filtrar por tipo
            _selectedType.value?.let { type ->
                try {
                    val response = RetrofitInstance.api.getTypeDetail(type.lowercase())
                    val validNames = response.pokemon.map { it.pokemon.name }.toSet()
                    currentList = currentList.filter { it.name in validNames }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            _filteredPokemons.value = currentList
            _isLoading.value = false
        }
    }

    fun setGenerationFilter(gen: String?) {
        _selectedGeneration.value = gen
        applyFilters()
    }

    fun setTypeFilter(type: String?) {
        _selectedType.value = type
        applyFilters()
    }
}
