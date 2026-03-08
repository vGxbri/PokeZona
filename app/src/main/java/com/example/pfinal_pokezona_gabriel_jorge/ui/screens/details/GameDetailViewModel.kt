//Gabriel Almarcha Martínez y Jorge Maqueda Miguel

package com.example.pfinal_pokezona_gabriel_jorge.ui.screens.details

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Modelos para leer los datos de Firebase
data class SavedPokemon(val name: String, val sprite: String)

data class SavedTeam(val id: String, val pokemons: List<SavedPokemon>)

class GameDetailViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _savedTeams = MutableStateFlow<List<SavedTeam>>(emptyList())
    val savedTeams: StateFlow<List<SavedTeam>> = _savedTeams.asStateFlow()

    fun listenToTeams(gameId: String) {
        val userId = auth.currentUser?.uid ?: return

        // Vemos los equipos de ese juego
        db.collection("users")
            .document(userId)
            .collection("games")
            .document(gameId)
            .collection("teams")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                if (snapshot != null) {
                    val teams =
                        snapshot.documents.mapNotNull { doc ->
                            try {
                                val pokemonsData =
                                    doc.get("pokemons") as? List<Any> ?: emptyList()
                                val pokemons =
                                    pokemonsData.mapNotNull { item ->
                                        when (item) {
                                            is Map<*, *> ->
                                                SavedPokemon(
                                                    name =
                                                        item["name"] as?
                                                                String
                                                            ?: "",
                                                    sprite =
                                                        item["sprite"] as?
                                                                String
                                                            ?: ""
                                                )

                                            is String ->
                                                SavedPokemon(
                                                    name = item,
                                                    sprite = ""
                                                )

                                            else -> null
                                        }
                                    }
                                SavedTeam(id = doc.id, pokemons = pokemons)
                            } catch (e: Exception) {
                                null
                            }
                        }
                    _savedTeams.value = teams
                }
            }
    }

    // Borramos un equipo
    fun deleteTeam(gameId: String, teamId: String) {
        val userId = auth.currentUser?.uid ?: return
        db.collection("users")
            .document(userId)
            .collection("games")
            .document(gameId)
            .collection("teams")
            .document(teamId)
            .delete()
    }
}
