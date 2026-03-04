package com.example.pfinal_pokezona_gabriel_jorge.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    // Esta es la URL base de toda la PokéAPI
    private const val BASE_URL = "https://pokeapi.co/api/v2/"

    // lazy hace que solo se cree la conexión la primera vez que se necesite
    val api: PokeApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Aquí usamos Gson para leer el JSON
            .build()
            .create(PokeApiService::class.java)
    }
}