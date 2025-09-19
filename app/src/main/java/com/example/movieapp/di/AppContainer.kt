package com.example.movieapp.di

import android.content.Context
import com.example.movieapp.data.MoviesRepository
import com.example.movieapp.data.MoviesRepositoryInterface
import com.example.movieapp.data.local.MovieDatabase
import com.example.movieapp.data.network.RetrofitClient

interface AppContainer {
    val movieRepository: MoviesRepositoryInterface
}

class DefaultAppContainer(
    context: Context,
) : AppContainer {
    override val movieRepository: MoviesRepositoryInterface by lazy {
        MoviesRepository(
            movieApiService = RetrofitClient.movieApiService,
            movieDao = MovieDatabase.getDatabase(context = context).movieDao(),
        )
    }
}
