package com.example.pfinal_pokezona_gabriel_jorge.data.network

import com.example.pfinal_pokezona_gabriel_jorge.data.model.GameListResponse
import com.example.pfinal_pokezona_gabriel_jorge.data.model.PokemonListResponse // <-- Nuevo import
import retrofit2.http.GET
import retrofit2.http.Query

interface PokeApiService {
    @GET("version")
    suspend fun getGames(
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0
    ): GameListResponse

    // <-- NUEVA PETICIÓN PARA LA POKÉDEX -->
    @GET("pokemon")
    suspend fun getPokemons(
        // Vamos a pedir los 151 originales para empezar y que cargue rápido.
        // ¡Si quieres los 1025 que existen, solo cambia este número!
        @Query("limit") limit: Int = 151,
        @Query("offset") offset: Int = 0
    ): PokemonListResponse
}