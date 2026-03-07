package com.example.pfinal_pokezona_gabriel_jorge.ui.screens.details

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import com.example.pfinal_pokezona_gabriel_jorge.data.repository.GameRepository

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailScreen(
        gameId: String,
        onBackClick: () -> Unit,
        onAddTeamClick: (String) -> Unit,
        viewModel: GameDetailViewModel = viewModel() // <-- Inyectamos el cerebro
) {
    val coverUrl = GameRepository.getCover(gameId)
    val displayName =
            remember(gameId) {
                gameId.replace("-", " ").split(" ").filter { it.isNotEmpty() }.joinToString(" ") {
                    it.replaceFirstChar { char -> char.uppercase() }
                }
            }
    val metadata = getGameMetadata(gameId)

    // Obtenemos los equipos en tiempo real
    val savedTeams by viewModel.savedTeams.collectAsState()

    // Cuando se abre la pantalla, le decimos que escuche este juego en concreto
    LaunchedEffect(gameId) { viewModel.listenToTeams(gameId) }

    Scaffold(
            topBar = {
                TopAppBar(
                        title = {},
                        navigationIcon = {
                            IconButton(
                                    onClick = { onBackClick() },
                                    colors =
                                            IconButtonDefaults.iconButtonColors(
                                                    containerColor =
                                                            MaterialTheme.colorScheme.surface.copy(
                                                                    alpha = 0.5f
                                                            )
                                            )
                            ) {
                                Icon(
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Volver"
                                )
                            }
                        },
                        colors =
                                TopAppBarDefaults.topAppBarColors(
                                        containerColor = Color.Transparent
                                )
                )
            }
    ) { _ ->
        LazyColumn(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            item {
                Box(modifier = Modifier.fillMaxWidth().height(350.dp)) {
                    AsyncImage(
                            model =
                                    ImageRequest.Builder(LocalContext.current)
                                            .data(coverUrl)
                                            .crossfade(true)
                                            .build(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize().blur(30.dp)
                    )
                    Box(
                            modifier =
                                    Modifier.fillMaxSize()
                                            .background(
                                                    Brush.verticalGradient(
                                                            colors =
                                                                    listOf(
                                                                            Color.Transparent,
                                                                            MaterialTheme
                                                                                    .colorScheme
                                                                                    .background
                                                                                    .copy(
                                                                                            alpha =
                                                                                                    0.8f
                                                                                    ),
                                                                            MaterialTheme
                                                                                    .colorScheme
                                                                                    .background
                                                                    ),
                                                            startY = 100f
                                                    )
                                            )
                    )
                    AsyncImage(
                            model =
                                    ImageRequest.Builder(LocalContext.current)
                                            .data(coverUrl)
                                            .crossfade(true)
                                            .build(),
                            contentDescription = "Portada de $displayName",
                            contentScale = ContentScale.Fit,
                            modifier =
                                    Modifier.align(Alignment.BottomCenter)
                                            .padding(bottom = 24.dp)
                                            .size(200.dp)
                    )
                }
            }

            item {
                Column(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                            "Pokémon $displayName",
                            style =
                                    MaterialTheme.typography.headlineMedium.copy(
                                            fontWeight = FontWeight.ExtraBold
                                    ),
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                    ) {
                        GameBadge(text = metadata.first, modifier = Modifier.weight(1f))
                        GameBadge(text = metadata.second, modifier = Modifier.weight(1f))
                        GameBadge(text = metadata.third, modifier = Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            item {
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
                    Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                    Icons.Default.Star,
                                    contentDescription = "Trofeo",
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                    "Mis Partidas",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 22.sp,
                                    color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        FilledTonalButton(
                                onClick = { onAddTeamClick(gameId) },
                                colors =
                                        ButtonDefaults.filledTonalButtonColors(
                                                containerColor =
                                                        MaterialTheme.colorScheme.primary.copy(
                                                                alpha = 0.1f
                                                        ),
                                                contentColor = MaterialTheme.colorScheme.primary
                                        )
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Añadir")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Añadir", fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // EL ESTADO VACÍO AHORA ES DINÁMICO
                    if (savedTeams.isEmpty()) {
                        Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors =
                                        CardDefaults.cardColors(
                                                containerColor =
                                                        MaterialTheme.colorScheme.surfaceVariant
                                                                .copy(alpha = 0.3f)
                                        ),
                                shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                        "Aún no has registrado ninguna partida en este juego.",
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }

            // DIBUJAMOS LOS EQUIPOS DE FIREBASE
            items(savedTeams) { team ->
                Box(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
                    TeamCard(team, onDelete = { viewModel.deleteTeam(gameId, team.id) })
                }
            }
        }
    }
}

@Composable
fun TeamCard(team: SavedTeam, onDelete: () -> Unit) {
    Card(
            modifier = Modifier.fillMaxWidth(),
            colors =
                    CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f)
                    ),
            shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                        text = "Hall de la Fama",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 14.sp
                )
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(
                            Icons.Default.Delete,
                            contentDescription = "Eliminar equipo",
                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                            modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
            ) {
                team.pokemons.forEach { pokemon ->
                    Box(
                            modifier =
                                    Modifier.size(42.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                    ) {
                        if (pokemon.sprite.isNotEmpty()) {
                            AsyncImage(
                                    model = pokemon.sprite,
                                    contentDescription = pokemon.name,
                                    modifier = Modifier.fillMaxSize().padding(4.dp)
                            )
                        } else {
                            // Por si dibuja el equipo antiguo que no tenía fotos
                            Text(
                                    pokemon.name.take(2).uppercase(),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GameBadge(text: String, modifier: Modifier = Modifier) {
    Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
            modifier = modifier
    ) {
        Text(
                text = text,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                maxLines = 1
        )
    }
}

fun getGameMetadata(gameId: String): Triple<String, String, String> {
    return when (gameId) {
        "red", "blue", "yellow" -> Triple("Gen 1", "Kanto", "Game Boy")
        "gold", "silver", "crystal" -> Triple("Gen 2", "Johto", "GB Color")
        "ruby", "sapphire", "emerald", "firered", "leafgreen" ->
                Triple("Gen 3", "Hoenn/Kanto", "GBA")
        "diamond", "pearl", "platinum", "heartgold", "soulsilver" ->
                Triple("Gen 4", "Sinnoh/Johto", "Nintendo DS")
        "black", "white", "black-2", "white-2" -> Triple("Gen 5", "Teselia", "Nintendo DS")
        "x", "y", "omega-ruby", "alpha-sapphire" -> Triple("Gen 6", "Kalos/Hoenn", "Nintendo 3DS")
        "sun", "moon", "ultra-sun", "ultra-moon" -> Triple("Gen 7", "Alola", "Nintendo 3DS")
        "lets-go-pikachu", "lets-go-eevee", "sword", "shield" ->
                Triple("Gen 8", "Galar/Kanto", "Switch")
        "brilliant-diamond", "shining-pearl", "legends-arceus", "scarlet", "violet" ->
                Triple("Gen 9", "Paldea/Hisui", "Switch")
        "legends-z-a" -> Triple("Gen 9", "Kalos", "Switch")
        else -> Triple("Desconocida", "Desconocida", "Desconocida")
    }
}
