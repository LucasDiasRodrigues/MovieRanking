package com.lucasrodrigues.themovieranking.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lucasrodrigues.themovieranking.model.Movie
import com.lucasrodrigues.themovieranking.util.MoviesResponseContainer
import com.lucasrodrigues.themovieranking.util.RetrofitUtils
import com.lucasrodrigues.themovieranking.util.TmdbInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.NullPointerException

class MovieDetailViewModel : ViewModel() {
    var movie = MutableLiveData<Movie>()
    private val tmdbApi by lazy {
        RetrofitUtils.instance.create(TmdbInterface::class.java)
    }

    fun getMovieDetail(m: Movie) {
        tmdbApi.getMovieDetails(m.id)
            .enqueue(object : Callback<Movie> {
                override fun onFailure(call: Call<Movie>, t: Throwable) {
                    Log.e("TMDB_call_error", t.message)
                }

                override fun onResponse(
                    call: Call<Movie>,
                    response: Response<Movie>
                ) {
                    if (response.code() == 200) {
                        try {
                            val receivedMovie = response.body()
                            Log.e("SUCESSO", response.body().toString())
                            receivedMovie?.genresString = m.genresString
                            movie.postValue(receivedMovie)
                        } catch (exception: NullPointerException) {
                            Log.e("TMDB_call_error", response.message())
                        }
                    }
                }
            })
    }


}
