package com.lucasrodrigues.themovieranking.util

import com.google.gson.annotations.SerializedName
import com.lucasrodrigues.themovieranking.model.Movie

data class MoviesResponseContainer(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val movies: List<Movie>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)