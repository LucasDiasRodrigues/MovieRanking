package com.lucasrodrigues.themovieranking.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lucasrodrigues.themovieranking.databinding.ItemMovieBinding
import com.lucasrodrigues.themovieranking.model.Movie
import com.lucasrodrigues.themovieranking.util.TmdbInterface

class MoviesAdapter(
    private val movieClickListener: MovieClickListenner,
    private val onBottomReached: onBottomReachedListenner
) :
    RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {
    private var movies = ArrayList<Movie>()

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
        if (position > (itemCount - 2) && itemCount > 10)
            onBottomReached.onBottomReached()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            movieClickListener
        )
    }

    fun addMoviesToList(movies: List<Movie>) {
        this.movies.addAll(movies)
        notifyDataSetChanged()
    }

    fun resetMovieList() {
        this.movies.clear()
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val binding: ItemMovieBinding,
        private val movieClick: MovieClickListenner
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.movie = movie
            binding.imagesBaseUrl = TmdbInterface.imagesBaseURL
            binding.executePendingBindings()
            itemView.setOnClickListener(movieClick.onMovieClick(movie))
        }
    }

    interface MovieClickListenner {
        fun onMovieClick(movie: Movie): View.OnClickListener
    }

    interface onBottomReachedListenner {
        fun onBottomReached()
    }
}

