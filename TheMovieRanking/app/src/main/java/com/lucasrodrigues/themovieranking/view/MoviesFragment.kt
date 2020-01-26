package com.lucasrodrigues.themovieranking.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.lucasrodrigues.themovieranking.R
import com.lucasrodrigues.themovieranking.model.Movie
import com.lucasrodrigues.themovieranking.viewmodel.MoviesFragmentViewModel
import kotlinx.android.synthetic.main.movies_fragment.*

class MoviesFragment : Fragment(),
    MoviesAdapter.MovieClickListenner,
    MoviesAdapter.onBottomReachedListenner {


    private val moviesAdapter: MoviesAdapter by lazy {
        MoviesAdapter(this, this)
    }
    private lateinit var viewModel: MoviesFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.movies_fragment, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.post { (activity as AppCompatActivity).setSupportActionBar(toolbar) }
        rv_movies.adapter = moviesAdapter
        rv_movies.hasFixedSize()

        viewModel = ViewModelProviders.of(this).get(MoviesFragmentViewModel::class.java)
        viewModel.getGenres()
        viewModel.movies.observe(viewLifecycleOwner, Observer {
            Log.i("MoviesFragment", "Observer")
            moviesAdapter.addMoviesToList(it)
        })
        viewModel.getMovies()
    }

    override fun onMovieClick(movie: Movie): View.OnClickListener {
        return View.OnClickListener {
            findNavController().navigate(
                MoviesFragmentDirections.movieSelected(
                    movie
                )
            )
        }
    }

    override fun onBottomReached() {
        viewModel.incrementPage()
        viewModel.getMovies()
    }
}
