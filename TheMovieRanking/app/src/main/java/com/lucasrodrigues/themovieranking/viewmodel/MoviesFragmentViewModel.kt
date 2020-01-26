package com.lucasrodrigues.themovieranking.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lucasrodrigues.themovieranking.model.Genre
import com.lucasrodrigues.themovieranking.model.Movie
import com.lucasrodrigues.themovieranking.util.GenresResponseContainer
import com.lucasrodrigues.themovieranking.util.MoviesResponseContainer
import com.lucasrodrigues.themovieranking.util.RetrofitUtils
import com.lucasrodrigues.themovieranking.util.TmdbInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.NullPointerException

class MoviesFragmentViewModel : ViewModel() {
    private val tmdbApi by lazy {
        RetrofitUtils.instance.create(TmdbInterface::class.java)
    }
    val movies = MutableLiveData<MutableList<Movie>>()
    val genres = ArrayList<Genre>()
    private var pagination: Int = 1

    fun incrementPage() {
        pagination++

    }

    fun resetPagination() {
        pagination = 1
    }

    fun getMovies() {
        tmdbApi.getPopularMovies(pagination)
            .enqueue(object : Callback<MoviesResponseContainer> {
                override fun onFailure(call: Call<MoviesResponseContainer>, t: Throwable) {
                    Log.e("TMDB_call_error", t.message)
                }

                override fun onResponse(
                    call: Call<MoviesResponseContainer>,
                    response: Response<MoviesResponseContainer>
                ) {
                    if (response.code() == 200) {
                        try {
                            val receivedMovies = response.body()!!.movies
                            receivedMovies.forEach {
                                it.genresString = setupGenres(it)
                            }

                            Log.i("PAGE", pagination.toString())
                                movies.postValue(receivedMovies as MutableList<Movie>)
                        } catch (exception: NullPointerException) {
                            Log.e("TMDB_call_error", response.message())
                        }
                    }
                }
            })
    }

    fun searchMovie(text: String) {
        tmdbApi.searchMovies(pagination, text)
            .enqueue(object : Callback<MoviesResponseContainer> {
                override fun onFailure(call: Call<MoviesResponseContainer>, t: Throwable) {
                    Log.e("TMDB_call_error", t.message)
                }

                override fun onResponse(
                    call: Call<MoviesResponseContainer>,
                    response: Response<MoviesResponseContainer>
                ) {
                    if (response.code() == 200) {
                        try {
                            val receivedMovies = response.body()!!.movies
                            receivedMovies.forEach {
                                it.genresString = setupGenres(it)
                            }

                            Log.i("PAGE", pagination.toString())
                            movies.postValue(receivedMovies as MutableList<Movie>)
                        } catch (exception: NullPointerException) {
                            Log.e("TMDB_call_error", response.message())
                        }
                    }
                }
            })
    }

    fun getGenres() {
        tmdbApi.getGenres().enqueue(object : Callback<GenresResponseContainer> {
            override fun onFailure(call: Call<GenresResponseContainer>, t: Throwable) {
                Log.e("TMDB_call_error", t.message)
            }

            override fun onResponse(
                call: Call<GenresResponseContainer>,
                response: Response<GenresResponseContainer>
            ) {
                if (response.code() == 200) {
                    try {
                        genres.clear()
                        genres.addAll(response.body()!!.genres)
                    } catch (exception: NullPointerException) {
                        Log.e("TMDB_call_error", response.message())
                    }
                }
            }

        })
    }

    fun setupGenres(movie: Movie): String {
        var genreString = ""
        movie.genres.forEach { id ->
            genres.forEach { genre ->
                if (id == genre.id)
                    genreString = genreString + genre.name + " | "
            }
        }
        return try {
            genreString.substring(0, genreString.length - 3)
        } catch (exception: StringIndexOutOfBoundsException) {
            ""
        }
    }
}
