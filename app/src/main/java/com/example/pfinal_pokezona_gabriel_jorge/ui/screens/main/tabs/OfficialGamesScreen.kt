package com.example.pfinal_pokezona_gabriel_jorge.ui.screens.main.tabs

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
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

@Composable
fun OfficialGamesScreen(onGameClick: (String) -> Unit, viewModel: GamesViewModel = viewModel()) {
        val games by viewModel.games.collectAsState()
        val completedGamesIds by viewModel.completedGamesIds.collectAsState()
        val isLoading by viewModel.isLoading.collectAsState()

        var completedExpanded by remember { mutableStateOf(true) }
        var allGamesExpanded by remember { mutableStateOf(false) }

        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                        text = "Juegos Oficiales",
                        style =
                                MaterialTheme.typography.headlineLarge.copy(
                                        fontWeight = FontWeight.ExtraBold
                                ),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 16.dp)
                )

                if (isLoading) {
                        Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                        ) { CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) }
                } else {
                        val completedGames = games.filter { completedGamesIds.contains(it.name) }
                        val pendingGames = games.filter { !completedGamesIds.contains(it.name) }

                        LazyVerticalStaggeredGrid(
                                columns = StaggeredGridCells.Fixed(2),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalItemSpacing = 16.dp,
                                contentPadding = PaddingValues(bottom = 140.dp)
                        ) {
                                // ═══ SECCIÓN: JUEGOS COMPLETADOS ═══
                                if (completedGames.isNotEmpty()) {
                                        item(span = StaggeredGridItemSpan.FullLine) {
                                                Column {
                                                        ExpandableSectionHeader(
                                                                title = "Juegos Completados",
                                                                count = completedGames.size,
                                                                isExpanded = completedExpanded,
                                                                onToggle = {
                                                                        completedExpanded =
                                                                                !completedExpanded
                                                                },
                                                                icon = Icons.Default.CheckCircle,
                                                                accentColor =
                                                                        MaterialTheme.colorScheme
                                                                                .primary
                                                        )
                                                        HorizontalDivider(
                                                                modifier =
                                                                        Modifier.padding(
                                                                                top = 4.dp,
                                                                                bottom = 8.dp
                                                                        ),
                                                                color =
                                                                        MaterialTheme.colorScheme
                                                                                .onBackground.copy(
                                                                                alpha = 0.2f
                                                                        )
                                                        )
                                                }
                                        }
                                        items(
                                                items = completedGames,
                                                key = { game -> "completed_${game.name}" }
                                        ) { game ->
                                                AnimatedVisibility(
                                                        visible = completedExpanded,
                                                        enter = fadeIn() + expandVertically(),
                                                        exit = fadeOut() + shrinkVertically()
                                                ) {
                                                        GameCard(
                                                                gameName = game.name,
                                                                onClick = { onGameClick(game.name) }
                                                        )
                                                }
                                        }
                                }

                                // ═══ SECCIÓN: TODOS LOS JUEGOS (sin completados) ═══
                                item(span = StaggeredGridItemSpan.FullLine) {
                                        Column {
                                                ExpandableSectionHeader(
                                                        title = "Todos los Juegos",
                                                        count = pendingGames.size,
                                                        isExpanded = allGamesExpanded,
                                                        onToggle = {
                                                                allGamesExpanded = !allGamesExpanded
                                                        },
                                                        icon = Icons.Default.SportsEsports,
                                                        accentColor =
                                                                MaterialTheme.colorScheme.secondary
                                                )
                                                HorizontalDivider(
                                                        modifier =
                                                                Modifier.padding(
                                                                        top = 4.dp,
                                                                        bottom = 8.dp
                                                                ),
                                                        color =
                                                                MaterialTheme.colorScheme
                                                                        .onBackground.copy(
                                                                        alpha = 0.2f
                                                                )
                                                )
                                        }
                                }
                                items(items = pendingGames, key = { game -> "all_${game.name}" }) {
                                        game ->
                                        AnimatedVisibility(
                                                visible = allGamesExpanded,
                                                enter = fadeIn() + expandVertically(),
                                                exit = fadeOut() + shrinkVertically()
                                        ) {
                                                GameCard(
                                                        gameName = game.name,
                                                        onClick = { onGameClick(game.name) }
                                                )
                                        }
                                }
                        }
                }
        }
}

@Composable
fun ExpandableSectionHeader(
        title: String,
        count: Int,
        isExpanded: Boolean,
        onToggle: () -> Unit,
        icon: ImageVector,
        accentColor: androidx.compose.ui.graphics.Color
) {
        val rotationAngle by
                animateFloatAsState(
                        targetValue = if (isExpanded) 180f else 0f,
                        animationSpec = tween(durationMillis = 300),
                        label = "rotation"
                )

        Surface(
                onClick = onToggle,
                color = androidx.compose.ui.graphics.Color.Transparent,
                shape = RoundedCornerShape(16.dp)
        ) {
                Row(
                        modifier =
                                Modifier.fillMaxWidth()
                                        .padding(vertical = 12.dp, horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                ) {
                        Box(modifier = Modifier.size(36.dp), contentAlignment = Alignment.Center) {
                                Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        tint = accentColor,
                                        modifier = Modifier.size(24.dp)
                                )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                                text = title,
                                style =
                                        MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold
                                        ),
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.weight(1f)
                        )
                        Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = androidx.compose.ui.graphics.Color.Transparent
                        ) {
                                Text(
                                        text = "$count",
                                        modifier =
                                                Modifier.padding(
                                                        horizontal = 8.dp,
                                                        vertical = 4.dp
                                                ),
                                        style =
                                                MaterialTheme.typography.titleMedium.copy(
                                                        fontWeight = FontWeight.Bold
                                                ),
                                        color = accentColor
                                )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = if (isExpanded) "Colapsar" else "Expandir",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier =
                                        Modifier.size(24.dp).graphicsLayer {
                                                rotationZ = rotationAngle
                                        }
                        )
                }
        }
}

@Composable
fun GameCard(gameName: String, onClick: () -> Unit) {
        val displayName =
                remember(gameName) {
                        gameName.replace("-", " ").split(" ").joinToString(" ") {
                                it.replaceFirstChar { char -> char.uppercase() }
                        }
                }

        Card(
                modifier = Modifier.fillMaxWidth().clickable { onClick() },
                shape =
                        RoundedCornerShape(
                                topStart = 4.dp,
                                topEnd = 4.dp,
                                bottomEnd = 12.dp,
                                bottomStart = 12.dp
                        ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors =
                        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
        ) {
                Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                ) {
                        AsyncImage(
                                model =
                                        ImageRequest.Builder(LocalContext.current)
                                                .data(GameRepository.getCover(gameName))
                                                .crossfade(true)
                                                .size(coil.size.Size.ORIGINAL)
                                                .build(),
                                contentDescription = "Portada de $displayName",
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier.fillMaxWidth()
                        )

                        Text(
                                text = displayName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center,
                                color =
                                        MaterialTheme.colorScheme
                                                .onSurface, // Color del texto dinámico
                                modifier = Modifier.padding(12.dp)
                        )
                }
        }
}
