package com.example.pfinal_pokezona_gabriel_jorge.ui.screens.details

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailScreen(gameId: String, onBackClick: () -> Unit) {
    Scaffold(
            topBar = {
                TopAppBar(
                        title = { Text(text = "Detalles: $gameId") },
                        navigationIcon = {
                            IconButton(onClick = { onBackClick() }) {
                                Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Volver"
                                )
                            }
                        }
                )
            }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
            Text(text = "Información sobre $gameId", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Aquí irán las partidas guardadas o datos extra.")
        }
    }
}
