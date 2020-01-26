package com.lucasrodrigues.themovieranking.view

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
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
            moviesAdapter.addMoviesToList(it)
        })
        viewModel.getMovies()
        sv_movies.setOnQueryTextListener(searchListenner())
        sv_movies.setOnCloseListener(searchCloseListener())
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
        if(viewModel.isSearching)
            viewModel.searchMovie(viewModel.lastSearchQuery)
        else
            viewModel.getMovies()
    }

    private fun searchListenner(): SearchView.OnQueryTextListener {
        return object : SearchView.OnQueryTextListener {
            private val handler = Handler()
            private var runnable = Runnable {}

            override fun onQueryTextSubmit(query: String?): Boolean {
                handler.removeCallbacks(runnable)
                if (!query.isNullOrEmpty()) {
                    viewModel.resetPagination()
                    moviesAdapter.resetMovieList()
                    viewModel.searchMovie(query)
                    viewModel.isSearching = true
                    viewModel.lastSearchQuery = query
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                handler.removeCallbacks(runnable)

                if (!newText.isNullOrEmpty() || newText?.trim() != "") {
                    runnable = Runnable {
                        onQueryTextSubmit(newText)
                    }
                    handler.postDelayed(runnable, 500)
                }
                return false
            }
        }
    }

    private fun searchCloseListener(): SearchView.OnCloseListener {
        return SearchView.OnCloseListener {
            viewModel.isSearching = false
            viewModel.resetPagination()
            moviesAdapter.resetMovieList()
            viewModel.getMovies()
            false
        }
    }
}
