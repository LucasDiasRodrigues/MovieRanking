package com.lucasrodrigues.themovieranking.view

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
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
        rv_movies.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(rv_movies.context, R.anim.layout_animation_fall_down)
        rv_movies.scheduleLayoutAnimation()

        viewModel = ViewModelProviders.of(this).get(MoviesFragmentViewModel::class.java)
        viewModel.movies.observe(viewLifecycleOwner, Observer {
            moviesAdapter.addMoviesToList(it)
        })

        getGenresFromAPI()
        getMoviesFromAPI()

        sv_movies.setOnQueryTextListener(searchListenner())
        sv_movies.setOnCloseListener(searchCloseListener())
    }

    fun getMoviesFromAPI() {
        if (checkInternetConnection())
            viewModel.getMovies()
        else
            connectionError()
    }

    fun getGenresFromAPI() {
        if (checkInternetConnection())
            viewModel.getGenres()
        else
            connectionError()
    }

    fun searchMovies(query: String) {
        if (checkInternetConnection())
            viewModel.searchMovie(query)
        else
            connectionError()
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
        if (viewModel.isSearching)
            searchMovies(viewModel.lastSearchQuery)
        else
            getMoviesFromAPI()
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
                    searchMovies(query)
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
            rv_movies.scheduleLayoutAnimation()
            getMoviesFromAPI()
            false
        }
    }

    fun checkInternetConnection(): Boolean {
        val connectivityManager: ConnectivityManager =
            activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    fun connectionError() {
        AlertDialog.Builder(activity as AppCompatActivity)
            .setTitle("Ops... ")
            .setMessage("Parece que vc não está conectado à internet :(\nTente novamente mais tarde")
            .create()
            .show()
    }
}
