package com.example.movieapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.movieapp.BuildConfig
import com.example.movieapp.data.local.MovieDao
import com.example.movieapp.data.local.MovieEntity
import com.example.movieapp.data.network.Movie
import com.example.movieapp.data.network.MovieApiService

interface MoviesRepositoryInterface {
    suspend fun getPopularMovies(page: Int): List<Movie>

    fun getFavoriteMovies(): LiveData<List<Movie>>

    suspend fun getFavoriteMovieById(id: Int): Movie?

    suspend fun addFavoriteMovie(movie: Movie)

    suspend fun removeFavoriteMovie(id: Int)
}

class MoviesRepository(
    private val movieApiService: MovieApiService,
    private val movieDao: MovieDao,
) : MoviesRepositoryInterface {
    private val apiKey = BuildConfig.API_KEY

    override suspend fun getPopularMovies(page: Int): List<Movie> = movieApiService.getPopularMovies(apiKey, page).results

    override fun getFavoriteMovies(): LiveData<List<Movie>> = movieDao.getAllMovies().map { list -> list.map { it.toMovie() } }

    override suspend fun getFavoriteMovieById(id: Int): Movie? = movieDao.getMovieById(id)?.toMovie()

    override suspend fun addFavoriteMovie(movie: Movie) {
        movieDao.insertMovie(movie.toMovieEntity())
    }

    override suspend fun removeFavoriteMovie(id: Int) {
        movieDao.deleteMovie(id)
    }

    private fun Movie.toMovieEntity(): MovieEntity =
        MovieEntity(
            id = id,
            title = title,
            overview = overview,
            posterPath = posterPath,
            releaseDate = releaseDate,
            voteAverage = voteAverage,
        )

    private fun MovieEntity.toMovie(): Movie =
        Movie(
            id = id,
            title = title,
            overview = overview,
            posterPath = posterPath,
            releaseDate = releaseDate,
            voteAverage = voteAverage,
        )
}
