package com.example.movieapp.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {
    @GET("popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1,
    ): MovieResponse
}
