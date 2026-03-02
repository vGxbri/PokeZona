package com.example.pfinal_pokezona_gabriel_jorge.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimario,
    secondary = DarkAcento,
    background = DarkFondo,
    surface = DarkSuperficie,
    onBackground = DarkTitulos,
    onSurface = DarkTitulos,
    onSurfaceVariant = DarkCuerpo
)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimario,
    secondary = LightAcento,
    background = LightFondo,
    surface = LightSuperficie,
    onBackground = LightTitulos,
    onSurface = LightTitulos,
    onSurfaceVariant = LightCuerpo
)

@Composable
fun PFinal_PokeZona_Gabriel_JorgeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}