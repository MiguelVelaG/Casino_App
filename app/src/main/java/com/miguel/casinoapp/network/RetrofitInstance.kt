package com.miguel.casinoapp.network

import com.miguel.casinoapp.data.DrawResponse
import com.miguel.casinoapp.data.DeckResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


interface ApiService {
    @GET("new/shuffle/")
    suspend fun shuffleDeck(): Response<DeckResponse>

    @GET("draw/")
    suspend fun drawCards(@Query("deck_id") deckId: String, @Query("count") count: Int): Response<DrawResponse>
}

object RetrofitInstance {
    private const val BASE_URL = "https://deckofcardsapi.com/api/deck/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
