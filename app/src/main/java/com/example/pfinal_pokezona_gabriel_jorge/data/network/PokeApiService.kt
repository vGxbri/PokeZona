package com.example.pfinal_pokezona_gabriel_jorge.data.network

import com.example.pfinal_pokezona_gabriel_jorge.data.model.GameListResponse
import com.example.pfinal_pokezona_gabriel_jorge.data.model.PokemonDetailResponse
import com.example.pfinal_pokezona_gabriel_jorge.data.model.PokemonListResponse
import com.example.pfinal_pokezona_gabriel_jorge.data.model.TypeDetailResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {
    @GET("version")
    suspend fun getGames(
            @Query("limit") limit: Int = 100,
            @Query("offset") offset: Int = 0
    ): GameListResponse

    @GET("pokemon")
    suspend fun getPokemons(
            @Query("limit") limit: Int = 151,
            @Query("offset") offset: Int = 0
    ): PokemonListResponse

    @GET("pokemon/{name}")
    suspend fun getPokemonDetail(@Path("name") name: String): PokemonDetailResponse

    @GET("type/{name}") suspend fun getTypeDetail(@Path("name") name: String): TypeDetailResponse
}
