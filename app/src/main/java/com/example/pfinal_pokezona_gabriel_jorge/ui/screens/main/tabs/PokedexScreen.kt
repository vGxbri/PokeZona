package com.example.pfinal_pokezona_gabriel_jorge.ui.screens.main.tabs

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
import coil.request.ImageRequest
import com.example.pfinal_pokezona_gabriel_jorge.data.model.PokemonResult
import coil.compose.SubcomposeAsyncImage

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
            text = "Pokédex",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.ExtraBold
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            // Usamos un Grid normal de 2 columnas para una colección perfectamente alineada
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                items(
                    items = pokemons,
                    key = { pokemon -> pokemon.name } // La Key sigue siendo clave para el rendimiento
                ) { pokemon ->
                    PokemonCard(pokemon = pokemon, onClick = { onPokemonClick(pokemon.name) })
                }
            }
        }
    }
}

// Fíjate en añadir este import arriba si no se pone solo:
// import coil.compose.SubcomposeAsyncImage

@Composable
fun PokemonCard(pokemon: PokemonResult, onClick: () -> Unit) {
    val displayName = remember(pokemon.name) {
        pokemon.name.replaceFirstChar { it.uppercase() }
    }

    val formattedId = remember(pokemon.name) {
        "#${pokemon.getPokemonId().padStart(3, '0')}"
    }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = formattedId,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.End)
            )

            // MAGIA AQUÍ: SubcomposeAsyncImage maneja estados de carga y error
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(pokemon.getImageUrl()) // Intenta cargar el HD primero
                    .crossfade(true)
                    .build(),
                contentDescription = "Imagen de $displayName",
                modifier = Modifier
                    .size(100.dp)
                    .padding(8.dp),
                loading = {
                    // Mientras carga la foto, sale una mini ruedita
                    CircularProgressIndicator(
                        modifier = Modifier.padding(32.dp),
                        strokeWidth = 2.dp
                    )
                },
                error = {
                    // Si el HD falla (404 Not Found), cargamos el sprite pixelado
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
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}