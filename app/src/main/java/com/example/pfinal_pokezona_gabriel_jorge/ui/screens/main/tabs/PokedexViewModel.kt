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
        db.collection("users").document(userId)
            .addSnapshotListener { snapshot, error ->
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
        // Usamos SetOptions.merge() para que cree el documento si el usuario es nuevo y aún no tiene uno
        db.collection("users").document(userId)
            .set(hashMapOf("favorites" to currentFavs.toList()), SetOptions.merge())
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
}