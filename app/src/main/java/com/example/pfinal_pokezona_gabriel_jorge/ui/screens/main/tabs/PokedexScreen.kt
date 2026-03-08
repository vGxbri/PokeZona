// Gabriel Almarcha Martínez y Jorge Maqueda Miguel

package com.example.pfinal_pokezona_gabriel_jorge.ui.screens.main.tabs

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.pfinal_pokezona_gabriel_jorge.R
import com.example.pfinal_pokezona_gabriel_jorge.data.model.PokemonResult
import com.example.pfinal_pokezona_gabriel_jorge.data.model.translateTypeToSpanish

@Composable
fun PokedexScreen(onPokemonClick: (String) -> Unit, viewModel: PokedexViewModel = viewModel()) {
    val pokemons by viewModel.filteredPokemons.collectAsState()
    val favoritePokemons by viewModel.favoritePokemons.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val selectedGeneration by viewModel.selectedGeneration.collectAsState()
    val selectedType by viewModel.selectedType.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val availableGenerations = viewModel.availableGenerations
    val availableTypes = viewModel.availableTypes

    var generationDropdownExpanded by remember { mutableStateOf(false) }
    var typeDropdownExpanded by remember { mutableStateOf(false) }
    var favsExpanded by remember { mutableStateOf(true) }
    var allPokemonExpanded by remember { mutableStateOf(true) }

    val gridState = rememberLazyGridState()
    var toolbarVisible by remember { mutableStateOf(true) }

    LaunchedEffect(gridState) {
        var previousIndex = 0
        var previousOffset = 0
        var accumulatedDelta = 0
        val scrollThreshold = 200
        snapshotFlow { gridState.firstVisibleItemIndex to gridState.firstVisibleItemScrollOffset }
            .collect { (index, offset) ->
                val delta = (index - previousIndex) * 500 + (offset - previousOffset)

                if (delta > 0) {
                    if (accumulatedDelta < 0) accumulatedDelta = 0
                    accumulatedDelta += delta
                    if (accumulatedDelta > scrollThreshold && index > 0) {
                        toolbarVisible = false
                        accumulatedDelta = 0
                    }
                } else if (delta < 0) {
                    if (accumulatedDelta > 0) accumulatedDelta = 0
                    accumulatedDelta += delta
                    if (accumulatedDelta < -scrollThreshold) {
                        toolbarVisible = true
                        accumulatedDelta = 0
                    }
                }
                if (index == 0 && offset == 0) {
                    toolbarVisible = true
                    accumulatedDelta = 0
                }
                previousIndex = index
                previousOffset = offset
            }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp)) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.pokedex),
            style =
                MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Buscador de Pokémons y filtros por Generación y Tipo
        AnimatedVisibility(
            visible = toolbarVisible,
            enter = fadeIn(tween(200)) + expandVertically(tween(250)),
            exit = fadeOut(tween(200)) + shrinkVertically(tween(250))
        ) {
            Column {
                // Buscador
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    placeholder = {
                        Text(
                            stringResource(R.string.search_pokemon),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = stringResource(R.string.search),
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = stringResource(R.string.clear),
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(28.dp),
                    colors =
                        OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.secondary,
                            unfocusedBorderColor =
                                MaterialTheme.colorScheme.secondary.copy(
                                    alpha = 0.3f
                                ),
                            cursorColor = MaterialTheme.colorScheme.secondary,
                            focusedContainerColor =
                                MaterialTheme.colorScheme.secondaryContainer.copy(
                                    alpha = 0.15f
                                ),
                            unfocusedContainerColor =
                                MaterialTheme.colorScheme.secondaryContainer.copy(
                                    alpha = 0.08f
                                )
                        ),
                    textStyle =
                        MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        )
                )

                // Filtros
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Generacion
                    Box(modifier = Modifier.weight(1f)) {
                        val genRotation by
                        animateFloatAsState(
                            targetValue = if (generationDropdownExpanded) 180f else 0f,
                            animationSpec = tween(durationMillis = 250),
                            label = "genArrow"
                        )
                        Surface(
                            onClick = { generationDropdownExpanded = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(28.dp),
                            color =
                                if (selectedGeneration != null)
                                    MaterialTheme.colorScheme.secondaryContainer.copy(
                                        alpha = 0.15f
                                    )
                                else
                                    MaterialTheme.colorScheme.secondaryContainer.copy(
                                        alpha = 0.08f
                                    ),
                            border =
                                BorderStroke(
                                    1.dp,
                                    if (selectedGeneration != null)
                                        MaterialTheme.colorScheme.secondary
                                    else
                                        MaterialTheme.colorScheme.secondary.copy(
                                            alpha = 0.3f
                                        )
                                )
                        ) {
                            Row(
                                modifier =
                                    Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = selectedGeneration
                                        ?: stringResource(R.string.generation),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight =
                                        if (selectedGeneration != null) FontWeight.SemiBold
                                        else FontWeight.Normal,
                                    color =
                                        if (selectedGeneration != null)
                                            MaterialTheme.colorScheme.onSurface
                                        else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = stringResource(R.string.expand),
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier =
                                        Modifier
                                            .size(20.dp)
                                            .graphicsLayer {
                                                rotationZ = genRotation
                                            }
                                )
                            }
                        }
                        DropdownMenu(
                            expanded = generationDropdownExpanded,
                            onDismissRequest = { generationDropdownExpanded = false },
                            modifier = Modifier
                                .width(180.dp)
                                .heightIn(max = 300.dp),
                            properties = PopupProperties(focusable = true)
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        stringResource(R.string.all_f),
                                        fontWeight =
                                            if (selectedGeneration == null)
                                                FontWeight.Bold
                                            else FontWeight.Normal
                                    )
                                },
                                onClick = {
                                    viewModel.setGenerationFilter(null)
                                    generationDropdownExpanded = false
                                },
                                trailingIcon = {
                                    if (selectedGeneration == null)
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                }
                            )
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp))
                            availableGenerations.forEach { gen ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            gen,
                                            fontWeight =
                                                if (selectedGeneration == gen)
                                                    FontWeight.Bold
                                                else FontWeight.Normal
                                        )
                                    },
                                    onClick = {
                                        viewModel.setGenerationFilter(gen)
                                        generationDropdownExpanded = false
                                    },
                                    trailingIcon = {
                                        if (selectedGeneration == gen)
                                            Icon(
                                                Icons.Default.Check,
                                                contentDescription = null,
                                                tint =
                                                    MaterialTheme.colorScheme
                                                        .primary,
                                                modifier = Modifier.size(18.dp)
                                            )
                                    }
                                )
                            }
                        }
                    }

                    // Tipo
                    Box(modifier = Modifier.weight(1f)) {
                        val typeRotation by
                        animateFloatAsState(
                            targetValue = if (typeDropdownExpanded) 180f else 0f,
                            animationSpec = tween(durationMillis = 250),
                            label = "typeArrow"
                        )
                        Surface(
                            onClick = { typeDropdownExpanded = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(28.dp),
                            color =
                                if (selectedType != null)
                                    MaterialTheme.colorScheme.secondaryContainer.copy(
                                        alpha = 0.15f
                                    )
                                else
                                    MaterialTheme.colorScheme.secondaryContainer.copy(
                                        alpha = 0.08f
                                    ),
                            border =
                                BorderStroke(
                                    1.dp,
                                    if (selectedType != null)
                                        MaterialTheme.colorScheme.secondary
                                    else
                                        MaterialTheme.colorScheme.secondary.copy(
                                            alpha = 0.3f
                                        )
                                )
                        ) {
                            Row(
                                modifier =
                                    Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = selectedType?.translateTypeToSpanish()
                                        ?: stringResource(R.string.type),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight =
                                        if (selectedType != null) FontWeight.SemiBold
                                        else FontWeight.Normal,
                                    color =
                                        if (selectedType != null)
                                            MaterialTheme.colorScheme.onSurface
                                        else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = stringResource(R.string.expand),
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier =
                                        Modifier
                                            .size(20.dp)
                                            .graphicsLayer {
                                                rotationZ = typeRotation
                                            }
                                )
                            }
                        }
                        DropdownMenu(
                            expanded = typeDropdownExpanded,
                            onDismissRequest = { typeDropdownExpanded = false },
                            modifier = Modifier
                                .width(180.dp)
                                .heightIn(max = 300.dp),
                            properties = PopupProperties(focusable = true)
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        stringResource(R.string.all_m),
                                        fontWeight =
                                            if (selectedType == null) FontWeight.Bold
                                            else FontWeight.Normal
                                    )
                                },
                                onClick = {
                                    viewModel.setTypeFilter(null)
                                    typeDropdownExpanded = false
                                },
                                trailingIcon = {
                                    if (selectedType == null)
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                }
                            )
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp))
                            availableTypes.forEach { type ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            type.translateTypeToSpanish(),
                                            fontWeight =
                                                if (selectedType == type)
                                                    FontWeight.Bold
                                                else FontWeight.Normal
                                        )
                                    },
                                    onClick = {
                                        viewModel.setTypeFilter(type)
                                        typeDropdownExpanded = false
                                    },
                                    trailingIcon = {
                                        if (selectedType == type)
                                            Icon(
                                                Icons.Default.Check,
                                                contentDescription = null,
                                                tint =
                                                    MaterialTheme.colorScheme
                                                        .primary,
                                                modifier = Modifier.size(18.dp)
                                            )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
            }
        } else {
            val favs = pokemons.filter { favoritePokemons.contains(it.name) }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                state = gridState,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 140.dp)
            ) {
                // Pokémons favoritos
                if (favs.isNotEmpty()) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Column {
                            ExpandableSectionHeader(
                                title = stringResource(R.string.favorites),
                                count = favs.size,
                                isExpanded = favsExpanded,
                                onToggle = { favsExpanded = !favsExpanded },
                                icon = Icons.Default.Favorite,
                                accentColor = MaterialTheme.colorScheme.primary
                            )
                            HorizontalDivider(
                                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
                                color =
                                    MaterialTheme.colorScheme.onBackground.copy(
                                        alpha = 0.2f
                                    )
                            )
                        }
                    }
                    items(items = favs, key = { pokemon -> "fav_${pokemon.name}" }) { pokemon ->
                        AnimatedVisibility(
                            visible = favsExpanded,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            PokemonCard(
                                pokemon = pokemon,
                                isFavorite = true,
                                onFavoriteClick = { viewModel.toggleFavorite(pokemon.name) },
                                onClick = { onPokemonClick(pokemon.name) }
                            )
                        }
                    }
                }

                // Todos los Pokémons
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column {
                        ExpandableSectionHeader(
                            title = stringResource(R.string.all_pokemon),
                            count = pokemons.size,
                            isExpanded = allPokemonExpanded,
                            onToggle = { allPokemonExpanded = !allPokemonExpanded },
                            icon = Icons.Default.FormatListNumbered,
                            accentColor = MaterialTheme.colorScheme.secondary
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                        )
                    }
                }

                items(items = pokemons, key = { pokemon -> "all_${pokemon.name}" }) { pokemon ->
                    AnimatedVisibility(
                        visible = allPokemonExpanded,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        PokemonCard(
                            pokemon = pokemon,
                            isFavorite = favoritePokemons.contains(pokemon.name),
                            onFavoriteClick = { viewModel.toggleFavorite(pokemon.name) },
                            onClick = { onPokemonClick(pokemon.name) }
                        )
                    }
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
    val displayName = remember(pokemon.name) { pokemon.name.replaceFirstChar { it.uppercase() } }

    val formattedId = remember(pokemon.name) { "#${pokemon.getPokemonId().padStart(3, '0')}" }

    val scale by
    animateFloatAsState(
        targetValue = if (isFavorite) 1.2f else 1f,
        animationSpec = tween(durationMillis = 200),
        label = "heartScale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                        imageVector =
                            if (isFavorite) Icons.Filled.Favorite
                            else Icons.Outlined.FavoriteBorder,
                        contentDescription = stringResource(R.string.favorite),
                        tint =
                            if (isFavorite) MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                    alpha = 0.4f
                                ),
                        modifier = Modifier.scale(scale)
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
                model =
                    ImageRequest.Builder(LocalContext.current)
                        .data(pokemon.getImageUrl())
                        .crossfade(true)
                        .build(),
                contentDescription = stringResource(R.string.image_of, displayName),
                modifier = Modifier
                    .size(90.dp)
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
                        contentDescription =
                            stringResource(R.string.sprite_of, displayName),
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
