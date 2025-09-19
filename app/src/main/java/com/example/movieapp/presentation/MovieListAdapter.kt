package com.example.movieapp.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.data.network.Movie

class MovieListAdapter(
    private val onItemClick: (Movie) -> Unit,
) : ListAdapter<Movie, MovieListAdapter.MovieViewHolder>(MovieDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_movie_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MovieViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    inner class MovieViewHolder(
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            val movieCard = itemView.findViewById<View>(R.id.cv_movie)
            val poster = itemView.findViewById<ImageView>(R.id.iv_movie)
            val title = itemView.findViewById<TextView>(R.id.tv_title)
            val overview = itemView.findViewById<TextView>(R.id.tv_overview)

            Glide
                .with(itemView.context)
                .load("https://image.tmdb.org/t/p/w500/${movie.posterPath}")
                .into(poster)

            title.text = movie.title
            overview.text = movie.overview
            movieCard.setOnClickListener {
                onItemClick(movie)
            }
        }
    }
}

class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(
        oldItem: Movie,
        newItem: Movie,
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: Movie,
        newItem: Movie,
    ): Boolean = oldItem == newItem
}
