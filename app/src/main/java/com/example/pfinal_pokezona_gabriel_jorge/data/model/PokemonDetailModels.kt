package com.example.pfinal_pokezona_gabriel_jorge.data.model

import com.google.gson.annotations.SerializedName

// Respuesta principal con todos los detalles del Pokémon
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
data class TypeSlot(
    @field:SerializedName("type") val type: NamedApiResource
)

data class StatSlot(
    @field:SerializedName("base_stat") val baseStat: Int,
    @field:SerializedName("stat") val stat: NamedApiResource
)

data class AbilitySlot(
    @field:SerializedName("ability") val ability: NamedApiResource,
    @field:SerializedName("is_hidden") val isHidden: Boolean
)

// Clase genérica para los objetos que solo traen un nombre y una url
data class NamedApiResource(
    @field:SerializedName("name") val name: String
)