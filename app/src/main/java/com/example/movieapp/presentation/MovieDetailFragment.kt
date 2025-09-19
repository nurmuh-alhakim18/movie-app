package com.example.movieapp.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.data.network.Movie
import kotlinx.coroutines.launch
import kotlin.getValue

class MovieDetailFragment : Fragment() {
    private val moviesViewModel: MoviesViewModel by viewModels { MoviesViewModel.Factory }
    private val poster: ImageView by lazy { requireView().findViewById(R.id.iv_poster) }
    private val title: TextView by lazy { requireView().findViewById(R.id.tv_title) }
    private val overview: TextView by lazy { requireView().findViewById(R.id.tv_overview) }
    private var movie: Movie? = null
    private val favoriteButton: Button by lazy { requireView().findViewById(R.id.btn_favorite) }
    private var isFavorite = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_detail, container, false)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val movieId = arguments?.getInt("MovieId") ?: return
        viewLifecycleOwner.lifecycleScope.launch {
            movie = moviesViewModel.getFavoriteMovieById(movieId)
            if (movie != null) {
                Glide
                    .with(requireContext())
                    .load("https://image.tmdb.org/t/p/w500/${movie!!.posterPath}")
                    .into(poster)
                title.text = movie!!.title
                overview.text = movie!!.overview
                isFavorite = true
                favoriteButton.text = getString(R.string.remove_from_favorites)
            } else {
                movie = arguments?.getParcelable<Movie>("Movie")
                movie?.let {
                    Glide
                        .with(requireContext())
                        .load("https://image.tmdb.org/t/p/w500/${it.posterPath}")
                        .into(poster)
                    title.text = it.title
                    overview.text = it.overview
                    favoriteButton.text = getString(R.string.add_to_favorites)
                }
            }
        }

        favoriteButton.setOnClickListener {
            onFavoriteButtonClick()
        }
    }

    private fun onFavoriteButtonClick() {
        movie?.let {
            viewLifecycleOwner.lifecycleScope.launch {
                if (isFavorite) {
                    moviesViewModel.removeFavoriteMovie(it.id)
                    favoriteButton.text = getString(R.string.add_to_favorites)
                } else {
                    moviesViewModel.addFavoriteMovie(it)
                    favoriteButton.text = getString(R.string.remove_from_favorites)
                }

                isFavorite = !isFavorite
            }
        }
    }
}
