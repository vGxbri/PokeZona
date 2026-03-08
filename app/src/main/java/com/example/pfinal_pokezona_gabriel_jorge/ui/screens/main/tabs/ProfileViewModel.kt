//Gabriel Almarcha Martínez y Jorge Maqueda Miguel

package com.example.pfinal_pokezona_gabriel_jorge.ui.screens.main.tabs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pfinal_pokezona_gabriel_jorge.data.model.PokemonResult
import com.example.pfinal_pokezona_gabriel_jorge.data.network.RetrofitInstance
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class PokemonStat(val name: String, val count: Int, val id: Int)

class ProfileViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    val userEmail = auth.currentUser?.email ?: "Entrenador"

    private val _userName = MutableStateFlow("Entrenador")
    val userName: StateFlow<String> = _userName.asStateFlow()

    // Detectar si la cuenta es de Google (no tiene contraseña)
    val isGoogleAccount: Boolean =
        auth.currentUser?.providerData?.any { it.providerId == "google.com" } == true

    private val _avatarPokemon = MutableStateFlow("pikachu")
    val avatarPokemon: StateFlow<String> = _avatarPokemon.asStateFlow()

    private val _avatarPokemonId = MutableStateFlow(25)
    val avatarPokemonId: StateFlow<Int> = _avatarPokemonId.asStateFlow()

    private val _topGames = MutableStateFlow<List<Pair<String, Int>>>(emptyList())
    val topGames: StateFlow<List<Pair<String, Int>>> = _topGames.asStateFlow()

    private val _topPokemons = MutableStateFlow<List<PokemonStat>>(emptyList())
    val topPokemons: StateFlow<List<PokemonStat>> = _topPokemons.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _allPokemons = MutableStateFlow<List<PokemonResult>>(emptyList())

    private val _avatarSearchText = MutableStateFlow("")
    val avatarSearchText: StateFlow<String> = _avatarSearchText.asStateFlow()

    private val _isPokemonListLoading = MutableStateFlow(false)
    val isPokemonListLoading: StateFlow<Boolean> = _isPokemonListLoading.asStateFlow()

    val filteredPokemons: StateFlow<List<PokemonResult>> =
        combine(_allPokemons, _avatarSearchText) { list, text ->
            if (text.isBlank()) list
            else list.filter { it.name.contains(text.lowercase()) }
        }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _passwordResetResult = MutableStateFlow<String?>(null)
    val passwordResetResult: StateFlow<String?> = _passwordResetResult.asStateFlow()

    init {
        loadProfileData()
    }

    fun loadProfileData() {
        val userId = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userDoc = db.collection("users").document(userId).get().await()
                if (userDoc.exists()) {
                    val savedAvatar = userDoc.getString("avatarPokemon")
                    if (!savedAvatar.isNullOrEmpty()) {
                        _avatarPokemon.value = savedAvatar
                        try {
                            val detail = RetrofitInstance.api.getPokemonDetail(savedAvatar)
                            _avatarPokemonId.value = detail.id
                        } catch (_: Exception) {
                        }
                    }
                }

                try {
                    val usuariosDoc = db.collection("usuarios").document(userId).get().await()
                    if (usuariosDoc.exists()) {
                        val nombre = usuariosDoc.getString("nombre")
                        if (!nombre.isNullOrBlank()) {
                            _userName.value = nombre
                        }
                    } else if (!auth.currentUser?.displayName.isNullOrBlank()) {
                        _userName.value = auth.currentUser?.displayName ?: "Entrenador"
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                val gamesList =
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
                        "violet"
                    )

                val gameCounts = mutableMapOf<String, Int>()
                val pokemonCounts = mutableMapOf<String, Int>()

                for (gameId in gamesList) {
                    val teamsSnapshot =
                        db.collection("users")
                            .document(userId)
                            .collection("games")
                            .document(gameId)
                            .collection("teams")
                            .get()
                            .await()

                    if (!teamsSnapshot.isEmpty) {
                        gameCounts[gameId] = teamsSnapshot.size()

                        for (doc in teamsSnapshot.documents) {
                            val pokemonsData = doc.get("pokemons") as? List<Any> ?: emptyList()
                            for (item in pokemonsData) {
                                val pokeName =
                                    when (item) {
                                        is Map<*, *> -> item["name"] as? String
                                        is String -> item
                                        else -> null
                                    }
                                if (pokeName != null) {
                                    pokemonCounts[pokeName] =
                                        pokemonCounts.getOrDefault(pokeName, 0) + 1
                                }
                            }
                        }
                    }
                }

                _topGames.value = gameCounts.toList().sortedByDescending { it.second }.take(3)

                val topPokemonPairs =
                    pokemonCounts.toList().sortedByDescending { it.second }.take(6)
                val resolvedPokemons =
                    topPokemonPairs.map { (name, count) ->
                        val pokemonId =
                            try {
                                RetrofitInstance.api.getPokemonDetail(name).id
                            } catch (_: Exception) {
                                1
                            }
                        PokemonStat(name, count, pokemonId)
                    }
                _topPokemons.value = resolvedPokemons
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateAvatar(pokemonName: String) {
        val userId = auth.currentUser?.uid ?: return
        _avatarPokemon.value = pokemonName
        db.collection("users").document(userId).update("avatarPokemon", pokemonName)
        viewModelScope.launch {
            try {
                val detail = RetrofitInstance.api.getPokemonDetail(pokemonName)
                _avatarPokemonId.value = detail.id
            } catch (_: Exception) {
            }
        }
    }


    fun fetchAllPokemons() {
        if (_allPokemons.value.isNotEmpty()) return // ya cargados
        viewModelScope.launch {
            _isPokemonListLoading.value = true
            try {
                val response = RetrofitInstance.api.getPokemons(limit = 1500)
                _allPokemons.value = response.results
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isPokemonListLoading.value = false
            }
        }
    }

    fun updateAvatarSearchText(text: String) {
        _avatarSearchText.value = text
    }


    fun changePassword(
        currentPassword: String,
        newPassword: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val user = auth.currentUser ?: return
        val email = user.email ?: return
        val credential = EmailAuthProvider.getCredential(email, currentPassword)

        user.reauthenticate(credential)
            .addOnSuccessListener {
                user.updatePassword(newPassword)
                    .addOnSuccessListener {
                        _passwordResetResult.value = "Contraseña actualizada correctamente."
                        onSuccess()
                    }
                    .addOnFailureListener { e ->
                        onError(e.message ?: "Error al actualizar la contraseña")
                    }
            }
            .addOnFailureListener { e -> onError("La contraseña actual es incorrecta.") }
    }

    fun clearPasswordResetResult() {
        _passwordResetResult.value = null
    }
}
