package com.example.movieapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.movieapp.MoviesApplication
import com.example.movieapp.data.MoviesRepositoryInterface
import com.example.movieapp.data.network.Movie
import kotlinx.coroutines.launch
import java.io.IOException

class MoviesViewModel(
    private val moviesRepository: MoviesRepositoryInterface,
) : ViewModel() {
    private val _popularMovies = MutableLiveData<List<Movie>>()
    private val movieList = mutableListOf<Movie>()
    val popularMovies: LiveData<List<Movie>> = _popularMovies

    val favoriteMovies: LiveData<List<Movie>> = moviesRepository.getFavoriteMovies()

    fun fetchPopularMovies(page: Int) {
        viewModelScope.launch {
            try {
                val movies = moviesRepository.getPopularMovies(page)
                movieList.addAll(movies)
                _popularMovies.postValue(movieList.toList())
            } catch (e: IOException) {
                // Handle error
            }
        }
    }

    suspend fun getFavoriteMovieById(id: Int): Movie? = moviesRepository.getFavoriteMovieById(id)

    fun addFavoriteMovie(movie: Movie) {
        viewModelScope.launch {
            moviesRepository.addFavoriteMovie(movie)
        }
    }

    fun removeFavoriteMovie(id: Int) {
        viewModelScope.launch {
            moviesRepository.removeFavoriteMovie(id)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val application = (this[APPLICATION_KEY] as MoviesApplication)
                    val moviesRepository = application.container.movieRepository
                    MoviesViewModel(moviesRepository = moviesRepository)
                }
            }
    }
}
