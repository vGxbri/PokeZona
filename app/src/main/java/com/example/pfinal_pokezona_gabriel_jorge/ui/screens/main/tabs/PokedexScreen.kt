package com.example.pfinal_pokezona_gabriel_jorge.ui.screens.main.tabs

import androidx.compose.foundation.BorderStroke // <-- Importante
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.pfinal_pokezona_gabriel_jorge.data.model.PokemonResult

@Composable
fun PokedexScreen(
    onPokemonClick: (String) -> Unit,
    viewModel: PokedexViewModel = viewModel()
) {
    val pokemons by viewModel.pokemons.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Pokédex Nacional",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.ExtraBold
            ),
            // AQUÍ USAMOS TU COLOR AZUL (Secundario) PARA VARIAR
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 140.dp)
            ) {
                items(
                    items = pokemons,
                    key = { pokemon -> pokemon.name }
                ) { pokemon ->
                    PokemonCard(pokemon = pokemon, onClick = { onPokemonClick(pokemon.name) })
                }
            }
        }
    }
}

@Composable
fun PokemonCard(pokemon: PokemonResult, onClick: () -> Unit) {
    val displayName = remember(pokemon.name) {
        pokemon.name.replaceFirstChar { it.uppercase() }
    }

    val formattedId = remember(pokemon.name) {
        "#${pokemon.getPokemonId().padStart(3, '0')}"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        // Cambiamos a cardElevation
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        // Cambiamos a cardColors
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f)
        ),
        // El borde azul
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = formattedId,
                // El número del Pokémon con tu color azul
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.End)
            )

            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(pokemon.getImageUrl())
                    .crossfade(true)
                    .build(),
                contentDescription = "Imagen de $displayName",
                modifier = Modifier
                    .size(100.dp)
                    .padding(8.dp),
                loading = {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(32.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                },
                error = {
                    AsyncImage(
                        model = pokemon.getSpriteUrl(),
                        contentDescription = "Sprite de $displayName",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            )

            Text(
                text = displayName,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}