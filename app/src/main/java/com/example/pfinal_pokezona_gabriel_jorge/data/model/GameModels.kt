//Gabriel Almarcha Martínez y Jorge Maqueda Miguel

package com.example.pfinal_pokezona_gabriel_jorge.data.model

import com.google.gson.annotations.SerializedName

// Esta clase representa la respuesta completa de la API
data class GameListResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("results") val results: List<GameResult>
)

// Esta clase representa cada juego individual en la lista
data class GameResult(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)