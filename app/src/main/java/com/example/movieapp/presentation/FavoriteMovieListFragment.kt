package com.example.movieapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R

class FavoriteMovieListFragment : Fragment() {
    private val moviesViewModel: MoviesViewModel by viewModels { MoviesViewModel.Factory }
    private lateinit var movieListAdapter: MovieListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieListAdapter =
            MovieListAdapter { movie ->
                val bundle = bundleOf("Movie" to movie, "MovieId" to movie.id)
                findNavController().navigate(R.id.action_movieFavoriteListFragment_to_movieDetailFragment, bundle)
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_movie_list, container, false)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        layoutManager = LinearLayoutManager(requireContext())
        initList()
        observeFavoriteMovies()
    }

    private fun initList() {
        view?.let {
            recyclerView = it.findViewById(R.id.rc_movie)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = movieListAdapter
        }
    }

    private fun observeFavoriteMovies() {
        moviesViewModel.favoriteMovies.observe(viewLifecycleOwner) {
            movieListAdapter.submitList(it)
        }
    }
}
