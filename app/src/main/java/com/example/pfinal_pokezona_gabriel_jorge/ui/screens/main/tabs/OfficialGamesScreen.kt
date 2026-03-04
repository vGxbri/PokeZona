package com.example.pfinal_pokezona_gabriel_jorge.ui.screens.main.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun OfficialGamesScreen(
    onGameClick: (String) -> Unit,
    viewModel: GamesViewModel = viewModel()
) {
    val games by viewModel.games.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Juegos Oficiales",
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
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalItemSpacing = 16.dp,
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                items(
                    items = games,
                    // Optimización CLAVE: Añadir una key. Esto le dice a Compose qué tarjeta es cuál,
                    // evitando que las redibuje por error cuando haces scroll hacia arriba y hacia abajo.
                    key = { game -> game.name }
                ) { game ->
                    GameCard(gameName = game.name, onClick = { onGameClick(game.name) })
                }
            }
        }
    }
}

@Composable
fun GameCard(gameName: String, onClick: () -> Unit) {
    // Optimización: Usar 'remember' guarda en caché el resultado del texto formateado.
    // Así no lo calcula 60 veces por segundo al hacer scroll.
    val displayName = remember(gameName) {
        gameName.replace("-", " ").split(" ").joinToString(" ") {
            it.replaceFirstChar { char -> char.uppercase() }
        }
    }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(GameRepository.getCover(gameName))
                    .crossfade(true)
                    // Optimización de Coil: Previene que intente cargar imágenes gigantescas,
                    // limitando la resolución a lo que cabe en la tarjeta y ahorrando muchísima RAM.
                    .size(coil.size.Size.ORIGINAL)
                    .build(),
                contentDescription = "Portada de $displayName",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    // Le damos una altura mínima para evitar el salto de 0px a Xpx
                    .defaultMinSize(minHeight = 150.dp)
                    // Ponemos un color de fondo temporal mientras se descarga la imagen
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            Text(
                text = displayName,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}