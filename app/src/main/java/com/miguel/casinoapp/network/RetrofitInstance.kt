package com.miguel.casinoapp.network

import com.miguel.casinoapp.data.DeckResponse
import com.miguel.casinoapp.data.DrawResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("new/shuffle/?deck_count=1")
    suspend fun shuffleDeck(): Response<DeckResponse>

    @GET("{deck_id}/draw/")
    suspend fun drawCards(
        @Path("deck_id") deckId: String,
        @Query("count") count: Int
    ): Response<DrawResponse>
}

object RetrofitInstance {
    private const val BASE_URL = "https://deckofcardsapi.com/api/deck/"

    val api: ApiService by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
