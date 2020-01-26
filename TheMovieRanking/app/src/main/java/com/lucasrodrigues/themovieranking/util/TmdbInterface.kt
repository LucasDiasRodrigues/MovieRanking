package com.lucasrodrigues.themovieranking.util

import com.lucasrodrigues.themovieranking.model.Movie
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbInterface {
    companion object {
        private const val API_KEY: String = "a2ba278da9c4de19e6b1e0b3e3166449"
        const val imagesBaseURL: String = "https://image.tmdb.org/t/p/w500"
    }

    @GET("discover/movie?language=pt-BR&sort_by=popularity.desc&include_video=false&api_key=$API_KEY")
    fun getPopularMovies(
        @Query("page") page: Int
    ): Call<MoviesResponseContainer>

    @GET("movie/{id}?language=pt-BR&api_key=$API_KEY")
    fun getMovieDetails(
        @Query("id") id: Int
    ): Call<Movie>

    @GET("genre/movie/list?language=pt-BR&api_key=$API_KEY")
    fun getGenres(): Call<GenresResponseContainer>
}