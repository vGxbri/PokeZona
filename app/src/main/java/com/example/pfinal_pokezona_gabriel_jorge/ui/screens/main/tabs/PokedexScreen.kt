package com.example.pfinal_pokezona_gabriel_jorge.ui.screens.main.tabs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PokedexScreen(onPokemonClick: (String) -> Unit) {
    val pokemons = listOf("Pikachu", "Charizard", "Bulbasaur", "Squirtle", "Mewtwo")

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Text(text = "Pokédex", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
        }
        items(pokemons) { pokemon ->
            Card(
                    modifier =
                            Modifier.fillMaxWidth().padding(vertical = 8.dp).clickable {
                                onPokemonClick(pokemon)
                            }
            ) { Text(text = pokemon, modifier = Modifier.padding(16.dp)) }
        }
    }
}
