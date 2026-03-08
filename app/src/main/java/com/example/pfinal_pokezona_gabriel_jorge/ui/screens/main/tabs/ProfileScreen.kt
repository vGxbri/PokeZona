// Gabriel Almarcha Martínez y Jorge Maqueda Miguel

package com.example.pfinal_pokezona_gabriel_jorge.ui.screens.main.tabs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.pfinal_pokezona_gabriel_jorge.R
import com.example.pfinal_pokezona_gabriel_jorge.data.repository.GameRepository

@Composable
fun ProfileScreen(onLogoutClick: () -> Unit, viewModel: ProfileViewModel = viewModel()) {
        val avatarPokemonId by viewModel.avatarPokemonId.collectAsState()
        val userName by viewModel.userName.collectAsState()
        val topGames by viewModel.topGames.collectAsState()
        val topPokemons by viewModel.topPokemons.collectAsState()
        val isLoading by viewModel.isLoading.collectAsState()
        val passwordResetResult by viewModel.passwordResetResult.collectAsState()

        var showAvatarPicker by remember { mutableStateOf(false) }
        var showLogoutDialog by remember { mutableStateOf(false) }
        var showChangePasswordDialog by remember { mutableStateOf(false) }

        val snackbarHostState = remember { SnackbarHostState() }

        LaunchedEffect(passwordResetResult) {
                passwordResetResult?.let {
                        snackbarHostState.showSnackbar(it)
                        viewModel.clearPasswordResetResult()
                }
        }

        LaunchedEffect(Unit) { viewModel.loadProfileData() }

        Box(modifier = Modifier.fillMaxSize()) {
                // Fondo degradado
                Box(
                        modifier =
                                Modifier.fillMaxWidth()
                                        .height(320.dp)
                                        .background(
                                                Brush.verticalGradient(
                                                        colors =
                                                                listOf(
                                                                        MaterialTheme.colorScheme
                                                                                .primary,
                                                                        MaterialTheme.colorScheme
                                                                                .primary.copy(
                                                                                alpha = 0.0f
                                                                        ),
                                                                        MaterialTheme.colorScheme
                                                                                .background
                                                                )
                                                )
                                        )
                )

                Column(
                        modifier =
                                Modifier.fillMaxSize()
                                        .padding(horizontal = 16.dp)
                                        .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                ) {
                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                                text = "Datos de Entrenador",
                                style =
                                        MaterialTheme.typography.headlineLarge.copy(
                                                fontWeight = FontWeight.ExtraBold
                                        ),
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                        )

                        // Entrenador
                        ElevatedCard(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(28.dp),
                                elevation =
                                        CardDefaults.elevatedCardElevation(
                                                defaultElevation = 12.dp
                                        ),
                                colors =
                                        CardDefaults.elevatedCardColors(
                                                containerColor = MaterialTheme.colorScheme.surface
                                        )
                        ) {
                                Column(
                                        modifier = Modifier.fillMaxWidth().padding(24.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                        // Avatar
                                        Box(
                                                modifier = Modifier.size(120.dp),
                                                contentAlignment = Alignment.BottomEnd
                                        ) {
                                                Box(
                                                        modifier =
                                                                Modifier.fillMaxSize()
                                                                        .clip(CircleShape)
                                                                        .background(
                                                                                Brush.radialGradient(
                                                                                        colors =
                                                                                                listOf(
                                                                                                        MaterialTheme
                                                                                                                .colorScheme
                                                                                                                .primary
                                                                                                                .copy(
                                                                                                                        alpha =
                                                                                                                                0.1f
                                                                                                                ),
                                                                                                        MaterialTheme
                                                                                                                .colorScheme
                                                                                                                .surface
                                                                                                )
                                                                                )
                                                                        )
                                                                        .border(
                                                                                3.dp,
                                                                                MaterialTheme
                                                                                        .colorScheme
                                                                                        .primary
                                                                                        .copy(
                                                                                                alpha =
                                                                                                        0.5f
                                                                                        ),
                                                                                CircleShape
                                                                        ),
                                                        contentAlignment = Alignment.Center
                                                ) {
                                                        AsyncImage(
                                                                model =
                                                                        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$avatarPokemonId.png",
                                                                contentDescription =
                                                                        stringResource(
                                                                                R.string.avatar
                                                                        ),
                                                                modifier = Modifier.size(85.dp),
                                                                contentScale = ContentScale.Fit
                                                        )
                                                }

                                                IconButton(
                                                        onClick = {
                                                                viewModel.fetchAllPokemons()
                                                                showAvatarPicker = true
                                                        },
                                                        modifier =
                                                                Modifier.size(34.dp)
                                                                        .background(
                                                                                MaterialTheme
                                                                                        .colorScheme
                                                                                        .primary,
                                                                                CircleShape
                                                                        )
                                                                        .border(
                                                                                2.dp,
                                                                                MaterialTheme
                                                                                        .colorScheme
                                                                                        .surface,
                                                                                CircleShape
                                                                        )
                                                ) {
                                                        Icon(
                                                                Icons.Rounded.Edit,
                                                                contentDescription =
                                                                        stringResource(
                                                                                R.string.edit_avatar
                                                                        ),
                                                                tint = Color.White,
                                                                modifier = Modifier.size(16.dp)
                                                        )
                                                }
                                        }

                                        Spacer(modifier = Modifier.height(16.dp))

                                        Text(
                                                text = userName,
                                                fontSize = 24.sp,
                                                fontWeight = FontWeight.Black,
                                                color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Surface(
                                                color =
                                                        MaterialTheme.colorScheme.primary.copy(
                                                                alpha = 0.08f
                                                        ),
                                                shape = RoundedCornerShape(12.dp)
                                        ) {
                                                Text(
                                                        text = viewModel.userEmail,
                                                        color =
                                                                MaterialTheme.colorScheme.onSurface
                                                                        .copy(alpha = 0.6f),
                                                        fontSize = 13.sp,
                                                        modifier =
                                                                Modifier.padding(
                                                                        horizontal = 12.dp,
                                                                        vertical = 6.dp
                                                                )
                                                )
                                        }
                                }
                        }

                        Spacer(modifier = Modifier.height(28.dp))

                        // Estadísticas
                        if (isLoading && topGames.isEmpty()) {
                                Spacer(modifier = Modifier.height(40.dp))
                                CircularProgressIndicator(
                                        color = MaterialTheme.colorScheme.primary,
                                        strokeWidth = 3.dp
                                )
                        } else {
                                // Juegos más jugados
                                SectionHeader(title = "Juegos Más Jugados")
                                Spacer(modifier = Modifier.height(12.dp))

                                if (topGames.isEmpty()) {
                                        EmptyStateCard(
                                                message =
                                                        "Todavía no has registrado ninguna aventura."
                                        )
                                } else {
                                        LazyRow(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                                contentPadding = PaddingValues(horizontal = 4.dp)
                                        ) { items(topGames) { game -> GameStatCard(game.first) } }
                                }

                                Spacer(modifier = Modifier.height(28.dp))

                                // Pokémons más usados
                                SectionHeader(title = "Tus MVP (Más Usados)")
                                Spacer(modifier = Modifier.height(12.dp))

                                if (topPokemons.isEmpty()) {
                                        EmptyStateCard(message = "Tus cajas del PC están vacías.")
                                } else {
                                        LazyRow(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(14.dp),
                                                contentPadding = PaddingValues(horizontal = 4.dp)
                                        ) {
                                                items(topPokemons) { pokemon ->
                                                        PokemonStatCard(pokemon)
                                                }
                                        }
                                }
                        }

                        Spacer(modifier = Modifier.height(36.dp))

                        // Ajustes del perfil
                        SectionHeader(title = "Ajustes de Cuenta")
                        Spacer(modifier = Modifier.height(12.dp))

                        // Si no es una cuenta de Google, mostramos cambiar contraseña
                        if (!viewModel.isGoogleAccount) {
                                OutlinedButton(
                                        onClick = { showChangePasswordDialog = true },
                                        modifier = Modifier.fillMaxWidth().height(52.dp),
                                        shape = RoundedCornerShape(16.dp),
                                        colors =
                                                ButtonDefaults.outlinedButtonColors(
                                                        contentColor =
                                                                MaterialTheme.colorScheme.onSurface
                                                )
                                ) {
                                        Icon(
                                                Icons.Default.LockReset,
                                                contentDescription = null,
                                                modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                                stringResource(R.string.change_password),
                                                fontWeight = FontWeight.SemiBold
                                        )
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                        }

                        Button(
                                onClick = { showLogoutDialog = true },
                                modifier = Modifier.fillMaxWidth().height(52.dp),
                                colors =
                                        ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.error
                                        ),
                                shape = RoundedCornerShape(16.dp)
                        ) {
                                Icon(
                                        Icons.Default.ExitToApp,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                        stringResource(R.string.logout),
                                        fontWeight = FontWeight.SemiBold
                                )
                        }

                        Spacer(modifier = Modifier.height(120.dp))
                }

                SnackbarHost(
                        hostState = snackbarHostState,
                        modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
                )
        }

        // Seleccionar avatar
        if (showAvatarPicker) {
                AvatarPickerDialog(
                        viewModel = viewModel,
                        onDismiss = { showAvatarPicker = false },
                        onSelect = { pokemonName ->
                                viewModel.updateAvatar(pokemonName)
                                showAvatarPicker = false
                        }
                )
        }

        // Cambiar de contraseña
        if (showChangePasswordDialog) {
                ChangePasswordDialog(
                        onDismiss = { showChangePasswordDialog = false },
                        onConfirm = { currentPw, newPw, onError ->
                                viewModel.changePassword(
                                        currentPassword = currentPw,
                                        newPassword = newPw,
                                        onSuccess = { showChangePasswordDialog = false },
                                        onError = { errorMsg -> onError(errorMsg) }
                                )
                        }
                )
        }

        // Confirmación de cerrar sesión
        if (showLogoutDialog) {
                AlertDialog(
                        onDismissRequest = { showLogoutDialog = false },
                        title = {
                                Text(stringResource(R.string.logout), fontWeight = FontWeight.Bold)
                        },
                        text = {
                                Text(
                                        "¿Seguro que quieres cerrar sesión? Tendrás que volver a iniciar sesión para acceder a tu cuenta."
                                )
                        },
                        confirmButton = {
                                Button(
                                        onClick = {
                                                showLogoutDialog = false
                                                onLogoutClick()
                                        },
                                        colors =
                                                ButtonDefaults.buttonColors(
                                                        containerColor =
                                                                MaterialTheme.colorScheme.error
                                                )
                                ) { Text(stringResource(R.string.logout)) }
                        },
                        dismissButton = {
                                TextButton(onClick = { showLogoutDialog = false }) {
                                        Text(stringResource(R.string.cancel))
                                }
                        }
                )
        }
}

@Composable
fun AvatarPickerDialog(
        viewModel: ProfileViewModel,
        onDismiss: () -> Unit,
        onSelect: (String) -> Unit
) {
        val searchText by viewModel.avatarSearchText.collectAsState()
        val filteredPokemons by viewModel.filteredPokemons.collectAsState()
        val isLoadingList by viewModel.isPokemonListLoading.collectAsState()

        Dialog(
                onDismissRequest = onDismiss,
                properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
                Card(
                        modifier = Modifier.fillMaxWidth(0.92f).fillMaxHeight(0.75f),
                        shape = RoundedCornerShape(28.dp),
                        colors =
                                CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surface
                                )
                ) {
                        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                                Text(
                                        text = "Elige tu Pokémon",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 22.sp,
                                        modifier = Modifier.padding(bottom = 12.dp)
                                )

                                // Buscador
                                OutlinedTextField(
                                        value = searchText,
                                        onValueChange = { viewModel.updateAvatarSearchText(it) },
                                        modifier = Modifier.fillMaxWidth(),
                                        placeholder = {
                                                Text(stringResource(R.string.search_pokemon))
                                        },
                                        leadingIcon = {
                                                Icon(
                                                        Icons.Default.Search,
                                                        contentDescription = null
                                                )
                                        },
                                        shape = RoundedCornerShape(24.dp),
                                        singleLine = true
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                if (isLoadingList && filteredPokemons.isEmpty()) {
                                        Box(
                                                modifier = Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center
                                        ) { CircularProgressIndicator() }
                                } else {
                                        LazyVerticalGrid(
                                                columns = GridCells.Fixed(3),
                                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                modifier = Modifier.fillMaxSize()
                                        ) {
                                                items(filteredPokemons, key = { it.name }) { pokemon
                                                        ->
                                                        AvatarPokemonCard(
                                                                name = pokemon.name,
                                                                spriteUrl = pokemon.getSpriteUrl(),
                                                                onClick = { onSelect(pokemon.name) }
                                                        )
                                                }
                                        }
                                }
                        }
                }
        }
}

@Composable
fun AvatarPokemonCard(name: String, spriteUrl: String, onClick: () -> Unit) {
        Card(
                modifier = Modifier.fillMaxWidth().clickable { onClick() },
                colors =
                        CardDefaults.cardColors(
                                containerColor =
                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                        ),
                shape = RoundedCornerShape(16.dp)
        ) {
                Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(8.dp)
                ) {
                        AsyncImage(
                                model = spriteUrl,
                                contentDescription = name,
                                modifier = Modifier.size(56.dp)
                        )
                        Text(
                                text = name.replaceFirstChar { it.uppercase() },
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                        )
                }
        }
}

@Composable
fun ChangePasswordDialog(
        onDismiss: () -> Unit,
        onConfirm: (currentPassword: String, newPassword: String, onError: (String) -> Unit) -> Unit
) {
        var currentPassword by remember { mutableStateOf("") }
        var newPassword by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        var errorMessage by remember { mutableStateOf<String?>(null) }

        Dialog(onDismissRequest = onDismiss) {
                Surface(
                        shape = RoundedCornerShape(24.dp),
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 6.dp,
                        modifier = Modifier.fillMaxWidth().wrapContentHeight()
                ) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                                AnimatedVisibility(
                                        visible = errorMessage != null,
                                        enter = slideInVertically { -it } + fadeIn(),
                                        exit = slideOutVertically { -it } + fadeOut(),
                                        modifier =
                                                Modifier.align(Alignment.TopCenter)
                                                        .padding(
                                                                horizontal = 16.dp,
                                                                vertical = 8.dp
                                                        )
                                                        .zIndex(1f)
                                ) {
                                        Surface(
                                                modifier = Modifier.fillMaxWidth(),
                                                color = MaterialTheme.colorScheme.errorContainer,
                                                shape = RoundedCornerShape(12.dp),
                                                shadowElevation = 6.dp
                                        ) {
                                                Row(
                                                        modifier =
                                                                Modifier.padding(
                                                                        horizontal = 16.dp,
                                                                        vertical = 12.dp
                                                                ),
                                                        verticalAlignment =
                                                                Alignment.CenterVertically
                                                ) {
                                                        Icon(
                                                                Icons.Default.Warning,
                                                                contentDescription = null,
                                                                tint =
                                                                        MaterialTheme.colorScheme
                                                                                .onErrorContainer,
                                                                modifier = Modifier.size(20.dp)
                                                        )
                                                        Spacer(modifier = Modifier.width(10.dp))
                                                        Text(
                                                                text = errorMessage ?: "",
                                                                color =
                                                                        MaterialTheme.colorScheme
                                                                                .onErrorContainer,
                                                                fontSize = 13.sp,
                                                                fontWeight = FontWeight.Medium,
                                                                modifier = Modifier.weight(1f)
                                                        )
                                                }
                                        }
                                }

                                // Contenido principal del diálogo
                                Column(
                                        modifier = Modifier.padding(24.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                        Text(
                                                text = "Cambiar Contraseña",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 20.sp,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier.padding(bottom = 16.dp)
                                        )

                                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                                OutlinedTextField(
                                                        value = currentPassword,
                                                        onValueChange = {
                                                                currentPassword = it
                                                                errorMessage = null
                                                        },
                                                        label = {
                                                                Text(
                                                                        stringResource(
                                                                                R.string
                                                                                        .current_password
                                                                        )
                                                                )
                                                        },
                                                        modifier = Modifier.fillMaxWidth(),
                                                        singleLine = true,
                                                        shape = RoundedCornerShape(16.dp)
                                                )
                                                OutlinedTextField(
                                                        value = newPassword,
                                                        onValueChange = {
                                                                newPassword = it
                                                                errorMessage = null
                                                        },
                                                        label = {
                                                                Text(
                                                                        stringResource(
                                                                                R.string
                                                                                        .new_password
                                                                        )
                                                                )
                                                        },
                                                        modifier = Modifier.fillMaxWidth(),
                                                        singleLine = true,
                                                        shape = RoundedCornerShape(16.dp)
                                                )
                                                OutlinedTextField(
                                                        value = confirmPassword,
                                                        onValueChange = {
                                                                confirmPassword = it
                                                                errorMessage = null
                                                        },
                                                        label = {
                                                                Text(
                                                                        stringResource(
                                                                                R.string
                                                                                        .confirm_new_password
                                                                        )
                                                                )
                                                        },
                                                        modifier = Modifier.fillMaxWidth(),
                                                        singleLine = true,
                                                        shape = RoundedCornerShape(16.dp)
                                                )
                                        }

                                        Spacer(modifier = Modifier.height(24.dp))

                                        Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.End
                                        ) {
                                                TextButton(onClick = onDismiss) {
                                                        Text(stringResource(R.string.cancel))
                                                }
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Button(
                                                        onClick = {
                                                                when {
                                                                        currentPassword.isBlank() ->
                                                                                errorMessage =
                                                                                        "Introduce tu contraseña actual"
                                                                        newPassword.length < 6 ->
                                                                                errorMessage =
                                                                                        "La nueva contraseña debe tener al menos 6 caracteres"
                                                                        newPassword !=
                                                                                confirmPassword ->
                                                                                errorMessage =
                                                                                        "Las contraseñas no coinciden"
                                                                        else -> {
                                                                                onConfirm(
                                                                                        currentPassword,
                                                                                        newPassword,
                                                                                        { errorMsg
                                                                                                ->
                                                                                                errorMessage =
                                                                                                        errorMsg
                                                                                        }
                                                                                )
                                                                        }
                                                                }
                                                        }
                                                ) { Text(stringResource(R.string.change)) }
                                        }
                                }
                        }
                }
        }
}

@Composable
fun SectionHeader(title: String, modifier: Modifier = Modifier) {
        Text(
                text = title,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = modifier.fillMaxWidth()
        )
}

@Composable
fun EmptyStateCard(message: String, modifier: Modifier = Modifier) {
        Card(
                modifier = modifier.fillMaxWidth(),
                colors =
                        CardDefaults.cardColors(
                                containerColor =
                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        ),
                shape = RoundedCornerShape(16.dp)
        ) {
                Text(
                        text = message,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(20.dp).fillMaxWidth(),
                        textAlign = TextAlign.Center
                )
        }
}

@Composable
fun GameStatCard(gameId: String) {
        val displayName =
                remember(gameId) {
                        gameId.replace("-", " ").split(" ").joinToString(" ") {
                                it.replaceFirstChar { char -> char.uppercase() }
                        }
                }

        ElevatedCard(
                modifier = Modifier.width(110.dp),
                shape = RoundedCornerShape(4.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
                colors =
                        CardDefaults.elevatedCardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                        )
        ) {
                SubcomposeAsyncImage(
                        model =
                                ImageRequest.Builder(LocalContext.current)
                                        .data(GameRepository.getCover(gameId))
                                        .crossfade(true)
                                        .build(),
                        contentDescription = stringResource(R.string.cover_of, displayName),
                        contentScale = ContentScale.Crop,
                        modifier =
                                Modifier.fillMaxWidth()
                                        .height(140.dp)
                                        .clip(RoundedCornerShape(4.dp)),
                        loading = {
                                Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                ) {
                                        CircularProgressIndicator(
                                                modifier = Modifier.size(20.dp),
                                                strokeWidth = 2.dp,
                                                color = MaterialTheme.colorScheme.primary
                                        )
                                }
                        }
                )
        }
}

@Composable
fun PokemonStatCard(pokemon: PokemonStat) {
        ElevatedCard(
                modifier = Modifier.width(120.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
                colors =
                        CardDefaults.elevatedCardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                        )
        ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                        Column(
                                modifier = Modifier.fillMaxWidth().padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                                SubcomposeAsyncImage(
                                        model =
                                                ImageRequest.Builder(LocalContext.current)
                                                        .data(
                                                                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemon.id}.png"
                                                        )
                                                        .crossfade(true)
                                                        .build(),
                                        contentDescription = pokemon.name,
                                        modifier = Modifier.size(72.dp),
                                        loading = {
                                                CircularProgressIndicator(
                                                        modifier = Modifier.padding(20.dp),
                                                        strokeWidth = 2.dp,
                                                        color = MaterialTheme.colorScheme.secondary
                                                )
                                        },
                                        error = {
                                                AsyncImage(
                                                        model =
                                                                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${pokemon.id}.png",
                                                        contentDescription = pokemon.name,
                                                        modifier = Modifier.size(72.dp)
                                                )
                                        }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                        text = pokemon.name.replaceFirstChar { it.uppercase() },
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        color = MaterialTheme.colorScheme.onSurface
                                )
                        }

                        Box(
                                modifier =
                                        Modifier.align(Alignment.TopEnd)
                                                .padding(6.dp)
                                                .size(24.dp)
                                                .background(
                                                        MaterialTheme.colorScheme.primary,
                                                        CircleShape
                                                ),
                                contentAlignment = Alignment.Center
                        ) {
                                Text(
                                        text = "${pokemon.count}",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Black
                                )
                        }
                }
        }
}
