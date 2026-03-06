package com.example.pfinal_pokezona_gabriel_jorge.ui.screens.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.pfinal_pokezona_gabriel_jorge.data.repository.GameRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailScreen(gameId: String, onBackClick: () -> Unit) {
    val coverUrl = GameRepository.getCover(gameId)
    val displayName = remember(gameId) {
        gameId.replace("-", " ").split(" ").joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
    }
    val metadata = getGameMetadata(gameId)

    Scaffold(
        topBar = {
            // Barra superior transparente para que se vea la imagen de fondo
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(
                        onClick = { onBackClick() },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->
        // Usamos LazyColumn para que sea scrolleable cuando haya muchos equipos guardados
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = 120.dp) // Espacio para el BottomNav
        ) {
            item {
                // --- CABECERA INMERSIVA TIPO SPOTIFY/NETFLIX ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                ) {
                    // Imagen difuminada de fondo
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(coverUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .blur(30.dp) // Efecto de desenfoque premium
                    )

                    // Degradado para fundir la imagen con el fondo de tu app
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        MaterialTheme.colorScheme.background.copy(alpha = 0.8f),
                                        MaterialTheme.colorScheme.background
                                    ),
                                    startY = 100f
                                )
                            )
                    )

                    // Carátula nítida centrada
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(coverUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Portada de $displayName",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 24.dp)
                            .size(200.dp)
                    )
                }
            }

            item {
                // --- INFORMACIÓN DEL JUEGO ---
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Pokémon $displayName",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Etiquetas (Región, Generación, Consola)
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
                // --- SECCIÓN: MIS PARTIDAS Y EQUIPOS ---
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = "Trofeo",
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Mis Partidas",
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        // Botón preparadísimo para abrir el menú de crear equipo
                        FilledTonalButton(
                            onClick = { /* TODO: Abrir pantalla de crear partida */ },
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Añadir")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Añadir", fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Estado vacío (Empty State) hasta que metamos Firebase
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Aún no has registrado ninguna partida en este juego.",
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

// --- COMPONENTES VISUALES ---

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

// --- FUNCIÓN DE UTILIDAD ---
// Devuelve: (Generación, Región, Consola)
fun getGameMetadata(gameId: String): Triple<String, String, String> {
    return when (gameId) {
        "red", "blue", "yellow" -> Triple("Gen 1", "Kanto", "Game Boy")
        "gold", "silver", "crystal" -> Triple("Gen 2", "Johto", "GB Color")
        "ruby", "sapphire", "emerald", "firered", "leafgreen" -> Triple("Gen 3", "Hoenn/Kanto", "GBA")
        "diamond", "pearl", "platinum", "heartgold", "soulsilver" -> Triple("Gen 4", "Sinnoh/Johto", "Nintendo DS")
        "black", "white", "black-2", "white-2" -> Triple("Gen 5", "Teselia", "Nintendo DS")
        "x", "y", "omega-ruby", "alpha-sapphire" -> Triple("Gen 6", "Kalos/Hoenn", "Nintendo 3DS")
        "sun", "moon", "ultra-sun", "ultra-moon" -> Triple("Gen 7", "Alola", "Nintendo 3DS")
        "lets-go-pikachu", "lets-go-eevee", "sword", "shield" -> Triple("Gen 8", "Galar/Kanto", "Switch")
        "brilliant-diamond", "shining-pearl", "legends-arceus", "scarlet", "violet" -> Triple("Gen 9", "Paldea/Hisui", "Switch")
        "legends-z-a" -> Triple("Gen 9", "Kalos", "Switch")
        else -> Triple("Desconocida", "Desconocida", "Desconocida")
    }
}