//Gabriel Almarcha Martínez y Jorge Maqueda Miguel

package com.example.pfinal_pokezona_gabriel_jorge.data.model

import com.google.gson.annotations.SerializedName

// Respuesta con todos los detalles del Pokémon
data class PokemonDetailResponse(
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("height") val height: Int, // La API lo da en decímetros
    @field:SerializedName("weight") val weight: Int, // La API lo da en hectogramos
    @field:SerializedName("types") val types: List<TypeSlot>,
    @field:SerializedName("stats") val stats: List<StatSlot>,
    @field:SerializedName("abilities") val abilities: List<AbilitySlot>
)

// Clases auxiliares para desglosar el JSON de la API
data class TypeSlot(@field:SerializedName("type") val type: NamedApiResource)

data class StatSlot(
    @field:SerializedName("base_stat") val baseStat: Int,
    @field:SerializedName("stat") val stat: NamedApiResource
)

data class AbilitySlot(
    @field:SerializedName("ability") val ability: NamedApiResource,
    @field:SerializedName("is_hidden") val isHidden: Boolean
)

data class NamedApiResource(@field:SerializedName("name") val name: String)

data class SpeciesResponse(
    @field:SerializedName("flavor_text_entries") val flavorTextEntries: List<FlavorTextEntry>
)

data class FlavorTextEntry(
    @field:SerializedName("flavor_text") val flavorText: String,
    @field:SerializedName("language") val language: NamedApiResource
)

// Traducir los tipos a español

fun String.translateTypeToSpanish(): String {
    return when (this.lowercase()) {
        "normal" -> "Normal"
        "fighting" -> "Lucha"
        "flying" -> "Volador"
        "poison" -> "Veneno"
        "ground" -> "Tierra"
        "rock" -> "Roca"
        "bug" -> "Bicho"
        "ghost" -> "Fantasma"
        "steel" -> "Acero"
        "fire" -> "Fuego"
        "water" -> "Agua"
        "grass" -> "Planta"
        "electric" -> "Eléctrico"
        "psychic" -> "Psíquico"
        "ice" -> "Hielo"
        "dragon" -> "Dragón"
        "dark" -> "Siniestro"
        "fairy" -> "Hada"
        "unknown" -> "Desconocido"
        "shadow" -> "Sombra"
        else ->
            this.replaceFirstChar {
                it.uppercase()
            }
    }
}

//Traducir las estadisticas base de Pokémon a español
fun String.translateStatToSpanish(): String {
    return when (this.lowercase()) {
        "hp" -> "PS"
        "attack" -> "Ataque"
        "defense" -> "Defensa"
        "special-attack" -> "Atq. Esp"
        "special-defense" -> "Def. Esp"
        "speed" -> "Velocidad"
        else -> this.replaceFirstChar { it.uppercase() }
    }
}
