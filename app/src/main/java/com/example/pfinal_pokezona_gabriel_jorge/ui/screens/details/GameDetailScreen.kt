// Gabriel Almarcha Martínez y Jorge Maqueda Miguel

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.pfinal_pokezona_gabriel_jorge.R
import com.example.pfinal_pokezona_gabriel_jorge.data.repository.GameRepository

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailScreen(
    gameId: String,
    onBackClick: () -> Unit,
    onAddTeamClick: (String) -> Unit,
    viewModel: GameDetailViewModel = viewModel()
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
                            contentDescription = stringResource(R.string.back)
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
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            item {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)) {
                    AsyncImage(
                        model =
                            ImageRequest.Builder(LocalContext.current)
                                .data(coverUrl)
                                .crossfade(true)
                                .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .blur(30.dp)
                    )
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize()
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
                        contentDescription = stringResource(R.string.cover_of, displayName),
                        contentScale = ContentScale.Fit,
                        modifier =
                            Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 24.dp)
                                .size(200.dp)
                    )
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        stringResource(R.string.pokemon_name, displayName),
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
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = stringResource(R.string.trophy),
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                stringResource(R.string.my_games),
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
                            Icon(
                                Icons.Default.Add,
                                contentDescription = stringResource(R.string.add)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(stringResource(R.string.add), fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

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
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    stringResource(R.string.no_games),
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }

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
                    text = stringResource(R.string.hall_of_fame),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 14.sp
                )
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete_team),
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
                            Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        if (pokemon.sprite.isNotEmpty()) {
                            AsyncImage(
                                model = pokemon.sprite,
                                contentDescription = pokemon.name,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(4.dp)
                            )
                        } else {
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

@Composable
fun getGameMetadata(gameId: String): Triple<String, String, String> {
    return when (gameId) {
        "red", "blue", "yellow" ->
            Triple(
                stringResource(R.string.gen_1),
                stringResource(R.string.kanto),
                stringResource(R.string.game_boy)
            )

        "gold", "silver", "crystal" ->
            Triple(
                stringResource(R.string.gen_2),
                stringResource(R.string.johto),
                stringResource(R.string.gb_color)
            )

        "ruby", "sapphire", "emerald", "firered", "leafgreen" ->
            Triple(
                stringResource(R.string.gen_3),
                stringResource(R.string.hoenn_kanto),
                stringResource(R.string.gba)
            )

        "diamond", "pearl", "platinum", "heartgold", "soulsilver" ->
            Triple(
                stringResource(R.string.gen_4),
                stringResource(R.string.sinnoh_johto),
                stringResource(R.string.nintendo_ds)
            )

        "black", "white", "black-2", "white-2" ->
            Triple(
                stringResource(R.string.gen_5),
                stringResource(R.string.teselia),
                stringResource(R.string.nintendo_ds)
            )

        "x", "y", "omega-ruby", "alpha-sapphire" ->
            Triple(
                stringResource(R.string.gen_6),
                stringResource(R.string.kalos_hoenn),
                stringResource(R.string.nintendo_3ds)
            )

        "sun", "moon", "ultra-sun", "ultra-moon" ->
            Triple(
                stringResource(R.string.gen_7),
                stringResource(R.string.alola),
                stringResource(R.string.nintendo_3ds)
            )

        "lets-go-pikachu", "lets-go-eevee", "sword", "shield" ->
            Triple(
                stringResource(R.string.gen_8),
                stringResource(R.string.galar_kanto),
                stringResource(R.string.switch_console)
            )

        "brilliant-diamond", "shining-pearl", "legends-arceus", "scarlet", "violet" ->
            Triple(
                stringResource(R.string.gen_9),
                stringResource(R.string.paldea_hisui),
                stringResource(R.string.switch_console)
            )

        "legends-z-a" ->
            Triple(
                stringResource(R.string.gen_9),
                stringResource(R.string.kalos),
                stringResource(R.string.switch_console)
            )

        else ->
            Triple(
                stringResource(R.string.unknown),
                stringResource(R.string.unknown),
                stringResource(R.string.unknown)
            )
    }
}
