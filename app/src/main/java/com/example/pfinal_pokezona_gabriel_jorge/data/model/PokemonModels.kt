package com.example.pfinal_pokezona_gabriel_jorge.data.model

import com.google.gson.annotations.SerializedName

data class PokemonListResponse(
        @field:SerializedName("count") val count: Int,
        @field:SerializedName("results") val results: List<PokemonResult>
)

data class PokemonResult(
        @field:SerializedName("name") val name: String,
        @field:SerializedName("url") val url: String
) {
    fun getPokemonId(): String {
        return url.trimEnd('/').substringAfterLast('/')
    }

    // El arte oficial en HD (el que puede fallar en formas raras)
    fun getImageUrl(): String {
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${getPokemonId()}.png"
    }

    // NUEVO: El sprite pixelado clásico (casi siempre existe)
    fun getSpriteUrl(): String {
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${getPokemonId()}.png"
    }
}

// Modelos para filtrar por Tipo
data class TypeDetailResponse(@field:SerializedName("pokemon") val pokemon: List<TypePokemon>)

data class TypePokemon(@field:SerializedName("pokemon") val pokemon: PokemonResult)
