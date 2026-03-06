package com.example.pfinal_pokezona_gabriel_jorge.ui.screens.main.tabs

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
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
    val favoritePokemons by viewModel.favoritePokemons.collectAsState() // Observamos los favoritos
    val isLoading by viewModel.isLoading.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Pokédex Nacional",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.ExtraBold
            ),
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
                    // Verificamos si este Pokémon en concreto está en la lista de favoritos
                    val isFavorite = favoritePokemons.contains(pokemon.name)

                    PokemonCard(
                        pokemon = pokemon,
                        isFavorite = isFavorite,
                        onFavoriteClick = { viewModel.toggleFavorite(pokemon.name) },
                        onClick = { onPokemonClick(pokemon.name) }
                    )
                }
            }
        }
    }
}

@Composable
fun PokemonCard(
    pokemon: PokemonResult,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit
) {
    val displayName = remember(pokemon.name) {
        pokemon.name.replaceFirstChar { it.uppercase() }
    }

    val formattedId = remember(pokemon.name) {
        "#${pokemon.getPokemonId().padStart(3, '0')}"
    }

    // Animación de latido para el corazón
    val scale by animateFloatAsState(
        targetValue = if (isFavorite) 1.2f else 1f,
        animationSpec = tween(durationMillis = 200),
        label = "heartScale"
    )

    Card( // Usamos Card en lugar de ElevatedCard como arreglamos antes
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp), // Reducimos un poco el padding global de la tarjeta
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Fila superior con el Corazón y el ID del Pokémon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconToggleButton(
                    checked = isFavorite,
                    onCheckedChange = { onFavoriteClick() },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                        modifier = Modifier.scale(scale) // Aplica la animación de latido
                    )
                }

                Text(
                    text = formattedId,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(end = 4.dp)
                )
            }

            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(pokemon.getImageUrl())
                    .crossfade(true)
                    .build(),
                contentDescription = "Imagen de $displayName",
                modifier = Modifier
                    .size(90.dp) // Un poco más pequeño para dar espacio al corazón
                    .padding(4.dp),
                loading = {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(24.dp),
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
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
            )
        }
    }
}