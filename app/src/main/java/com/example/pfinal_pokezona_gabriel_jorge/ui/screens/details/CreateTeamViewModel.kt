//Gabriel Almarcha Martínez y Jorge Maqueda Miguel

package com.example.pfinal_pokezona_gabriel_jorge.ui.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pfinal_pokezona_gabriel_jorge.data.model.PokemonResult
import com.example.pfinal_pokezona_gabriel_jorge.data.network.RetrofitInstance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CreateTeamViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _pokemons = MutableStateFlow<List<PokemonResult>>(emptyList())

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Buscador
    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    // Mezcla la lista completa con lo que el usuario escriba en el buscador en tiempo real
    val filteredPokemons: StateFlow<List<PokemonResult>> =
        combine(_pokemons, _searchText) { list, text ->
            if (text.isBlank()) list else list.filter { it.name.contains(text.lowercase()) }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // El equipo de 6 Pokémons
    private val _selectedTeam = MutableStateFlow<List<PokemonResult>>(emptyList())
    val selectedTeam: StateFlow<List<PokemonResult>> = _selectedTeam.asStateFlow()

    // Favoritos
    private val _favoritePokemons = MutableStateFlow<Set<String>>(emptySet())
    val favoritePokemons: StateFlow<Set<String>> = _favoritePokemons.asStateFlow()

    // Si ya se guardó, se cierra la pantalla
    private val _saveSuccess = MutableStateFlow(false)
    val saveSuccess: StateFlow<Boolean> = _saveSuccess.asStateFlow()

    init {
        fetchPokemons()
        listenToFavorites()
    }

    private fun fetchPokemons() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = RetrofitInstance.api.getPokemons(limit = 1500)
                _pokemons.value = response.results
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun listenToFavorites() {
        val userId = auth.currentUser?.uid ?: return
        db.collection("users").document(userId).addSnapshotListener { snapshot, error ->
            if (error != null) return@addSnapshotListener
            if (snapshot != null && snapshot.exists()) {
                val favs = snapshot.get("favorites") as? List<String> ?: emptyList()
                _favoritePokemons.value = favs.toSet()
            }
        }
    }

    fun updateSearchText(text: String) {
        _searchText.value = text
    }

    fun toggleSelection(pokemon: PokemonResult) {
        val current = _selectedTeam.value.toMutableList()
        // Si ya está, lo quitamos. Si no está y hay menos de 6, lo metemos.
        if (current.any { it.name == pokemon.name }) {
            current.removeAll { it.name == pokemon.name }
        } else if (current.size < 6) {
            current.add(pokemon)
        }
        _selectedTeam.value = current
    }

    fun saveTeamToFirebase(gameId: String) {
        val userId = auth.currentUser?.uid ?: return
        if (_selectedTeam.value.size != 6) return

        _isLoading.value = true
        val teamData = hashMapOf(
            "timestamp" to FieldValue.serverTimestamp(),
            "pokemons" to _selectedTeam.value.map {
                mapOf("name" to it.name, "sprite" to it.getSpriteUrl())
            }
        )

        // Lo guardamos en los equipos del usuario
        db.collection("users").document(userId)
            .collection("games").document(gameId)
            .collection("teams").add(teamData)
            .addOnSuccessListener {
                _isLoading.value = false
                _saveSuccess.value = true
            }
            .addOnFailureListener {
                _isLoading.value = false
            }
    }
}