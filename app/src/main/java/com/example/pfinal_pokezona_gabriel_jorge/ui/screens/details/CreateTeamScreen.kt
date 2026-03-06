package com.example.pfinal_pokezona_gabriel_jorge.ui.screens.details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.pfinal_pokezona_gabriel_jorge.data.model.PokemonResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTeamScreen(
    gameId: String,
    onBackClick: () -> Unit,
    viewModel: CreateTeamViewModel = viewModel()
) {
    val searchText by viewModel.searchText.collectAsState()
    val filteredPokemons by viewModel.filteredPokemons.collectAsState()
    val selectedTeam by viewModel.selectedTeam.collectAsState()
    val favoritePokemons by viewModel.favoritePokemons.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val saveSuccess by viewModel.saveSuccess.collectAsState()

    // Si se guarda con éxito, volvemos atrás automáticamente
    LaunchedEffect(saveSuccess) {
        if (saveSuccess) onBackClick()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Equipo", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    // Botón de guardar (se desactiva si no hay 6)
                    TextButton(
                        onClick = { viewModel.saveTeamToFirebase(gameId) },
                        enabled = selectedTeam.size == 6 && !isLoading
                    ) {
                        Text("Guardar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 1. EL BUSCADOR
            OutlinedTextField(
                value = searchText,
                onValueChange = { viewModel.updateSearchText(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Buscar Pokémon...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(24.dp),
                singleLine = true
            )

            // 2. LOS 6 HUECOS DEL EQUIPO
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    for (i in 0 until 6) {
                        val pokemon = selectedTeam.getOrNull(i)
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(if (pokemon != null) Color.White else MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { if (pokemon != null) viewModel.toggleSelection(pokemon) },
                            contentAlignment = Alignment.Center
                        ) {
                            if (pokemon != null) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current).data(pokemon.getSpriteUrl()).build(),
                                    contentDescription = pokemon.name,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Icon(Icons.Default.Add, contentDescription = "Añadir", tint = Color.Gray)
                            }
                        }
                    }
                }
            }

            // 3. LA CUADRÍCULA PARA ELEGIR
            if (isLoading && filteredPokemons.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3), // 3 columnas para que quepan más al buscar
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredPokemons, key = { it.name }) { pokemon ->
                        val isSelected = selectedTeam.any { it.name == pokemon.name }
                        val isFavorite = favoritePokemons.contains(pokemon.name)

                        SelectablePokemonCard(
                            pokemon = pokemon,
                            isSelected = isSelected,
                            isFavorite = isFavorite,
                            onClick = { viewModel.toggleSelection(pokemon) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SelectablePokemonCard(
    pokemon: PokemonResult,
    isSelected: Boolean,
    isFavorite: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(8.dp)) {
            // Icono de corazón si es favorito
            if (isFavorite) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.TopEnd).size(16.dp)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AsyncImage(
                    model = pokemon.getSpriteUrl(),
                    contentDescription = null,
                    modifier = Modifier.size(60.dp)
                )
                Text(
                    text = pokemon.name.replaceFirstChar { it.uppercase() },
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    maxLines = 1
                )
            }
        }
    }
}