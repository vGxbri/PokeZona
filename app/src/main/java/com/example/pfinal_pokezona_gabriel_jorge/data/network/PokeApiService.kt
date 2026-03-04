package com.example.pfinal_pokezona_gabriel_jorge.data.network

import com.example.pfinal_pokezona_gabriel_jorge.data.model.GameListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PokeApiService {
    // Le indicamos que haga una petición GET a la ruta "version"
    @GET("version")
    suspend fun getGames(
        @Query("limit") limit: Int = 50, // Ponemos 50 para que nos traiga la mayoría de golpe
        @Query("offset") offset: Int = 0
    ): GameListResponse
}