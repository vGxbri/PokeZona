//Gabriel Almarcha Martínez y Jorge Maqueda Miguel

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

    // Imagenes de los Pokémon (a veces fallan)
    fun getImageUrl(): String {
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${getPokemonId()}.png"
    }

    // Sprites de los Pokémon (no suele fallar)
    fun getSpriteUrl(): String {
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${getPokemonId()}.png"
    }
}

// Modelos para filtrar por Tipo del Pokémon
data class TypeDetailResponse(@field:SerializedName("pokemon") val pokemon: List<TypePokemon>)

data class TypePokemon(@field:SerializedName("pokemon") val pokemon: PokemonResult)
