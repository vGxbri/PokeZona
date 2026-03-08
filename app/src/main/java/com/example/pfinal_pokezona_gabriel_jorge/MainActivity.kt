//Gabriel Almarcha Martínez y Jorge Maqueda Miguel

package com.example.pfinal_pokezona_gabriel_jorge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.pfinal_pokezona_gabriel_jorge.ui.navigation.AppNavigation
import com.example.pfinal_pokezona_gabriel_jorge.ui.theme.PFinal_PokeZona_Gabriel_JorgeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { PFinal_PokeZona_Gabriel_JorgeTheme { AppNavigation() } }
    }
}
