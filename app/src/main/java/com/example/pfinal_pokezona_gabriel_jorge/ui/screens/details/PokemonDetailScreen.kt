package com.example.pfinal_pokezona_gabriel_jorge.ui.screens.details

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.pfinal_pokezona_gabriel_jorge.data.model.PokemonDetailResponse
import com.example.pfinal_pokezona_gabriel_jorge.data.model.StatSlot

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(
    pokemonId: String,
    onBackClick: () -> Unit,
    viewModel: PokemonDetailViewModel = viewModel()
) {
    val pokemonDetail by viewModel.pokemonDetail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // LaunchedEffect hace que se ejecute la petición a internet nada más abrirse la pantalla
    LaunchedEffect(pokemonId) {
        viewModel.fetchPokemonDetail(pokemonId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = pokemonId.replaceFirstChar { it.uppercase() },
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        if (isLoading || pokemonDetail == null) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            val detail = pokemonDetail!!

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    // 1. Imagen del Pokémon en grande
                    val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${detail.id}.png"
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = detail.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(220.dp)
                            .padding(16.dp)
                    )
                }

                item {
                    // 2. Tipos (Badges con color)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(bottom = 24.dp)
                    ) {
                        detail.types.forEach { typeSlot ->
                            Surface(
                                shape = RoundedCornerShape(24.dp),
                                color = getTypeColor(typeSlot.type.name),
                            ) {
                                Text(
                                    text = typeSlot.type.name.uppercase(),
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }

                item {
                    // 3. Info Física (Peso y Altura)
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        InfoBox(title = "PESO", value = "${detail.weight / 10f} KG")
                        InfoBox(title = "ALTURA", value = "${detail.height / 10f} M")
                    }
                }

                item {
                    // 4. Estadísticas (Stats)
                    Text(
                        text = "Estadísticas Base",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                }

                items(detail.stats.size) { index ->
                    StatBar(stat = detail.stats[index])
                }

                item {
                    // 5. Habilidades
                    Text(
                        text = "Habilidades",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        modifier = Modifier.padding(top = 32.dp, bottom = 16.dp).fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )

                    detail.abilities.forEach { abilitySlot ->
                        val hiddenText = if (abilitySlot.isHidden) " (Oculta)" else ""
                        Text(
                            text = "• ${abilitySlot.ability.name.replaceFirstChar { it.uppercase() }}$hiddenText",
                            fontSize = 18.sp,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(48.dp))
                }
            }
        }
    }
}

// --- COMPONENTES VISUALES ---

@Composable
fun InfoBox(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontWeight = FontWeight.Bold, fontSize = 24.sp)
        Text(text = title, color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun StatBar(stat: StatSlot) {
    // Animación fluida para que las barras se llenen al cargar la pantalla
    var progress by remember { mutableStateOf(0f) }
    val targetProgress = stat.baseStat / 255f // 255 es el máximo teórico en Pokémon

    LaunchedEffect(stat) {
        progress = targetProgress
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000), // Tarda 1 segundo en llenarse
        label = "StatProgress"
    )

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = formatStatName(stat.stat.name),
            modifier = Modifier.weight(0.25f),
            color = Color.Gray,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Text(
            text = stat.baseStat.toString().padStart(3, '0'), // Rellena con ceros para que queden alineados
            modifier = Modifier.weight(0.15f),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .weight(0.6f)
                .height(10.dp)
                .clip(RoundedCornerShape(5.dp)),
            color = getStatColor(stat.baseStat),
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

// Formatea los nombres internos de la API a siglas clásicas
fun formatStatName(name: String): String {
    return when (name) {
        "hp" -> "HP"
        "attack" -> "ATK"
        "defense" -> "DEF"
        "special-attack" -> "SpA"
        "special-defense" -> "SpD"
        "speed" -> "SPD"
        else -> name.uppercase()
    }
}

// Colorea la barra según si la estadística es mala, normal o buena
fun getStatColor(value: Int): Color {
    return when {
        value < 50 -> Color(0xFFFF5252) // Rojo
        value < 90 -> Color(0xFFFFD740) // Naranja
        else -> Color(0xFF69F0AE) // Verde
    }
}

// Devuelve el color oficial de cada tipo
fun getTypeColor(type: String): Color {
    return when (type) {
        "normal" -> Color(0xFFA8A77A)
        "fire" -> Color(0xFFEE8130)
        "water" -> Color(0xFF6390F0)
        "electric" -> Color(0xFFF7D02C)
        "grass" -> Color(0xFF7AC74C)
        "ice" -> Color(0xFF96D9D6)
        "fighting" -> Color(0xFFC22E28)
        "poison" -> Color(0xFFA33EA1)
        "ground" -> Color(0xFFE2BF65)
        "flying" -> Color(0xFFA98FF3)
        "psychic" -> Color(0xFFF95587)
        "bug" -> Color(0xFFA6B91A)
        "rock" -> Color(0xFFB6A136)
        "ghost" -> Color(0xFF735797)
        "dragon" -> Color(0xFF6F35FC)
        "dark" -> Color(0xFF705848)
        "steel" -> Color(0xFFB7B7CE)
        "fairy" -> Color(0xFFD685AD)
        else -> Color(0xFF68A090)
    }
}