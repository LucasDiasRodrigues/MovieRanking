package com.lucasrodrigues.themovieranking.model

import com.google.gson.annotations.SerializedName
import com.lucasrodrigues.themovieranking.util.TmdbInterface
import java.io.Serializable

data class Movie(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("original_title") val originalTitle: String,
    @SerializedName("genre_ids") val genres: List<Int>,
    @SerializedName("overview") val overview: String,
    @SerializedName("release_date") private val releaseDate: String,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("budget") val budget: Double?,
    @SerializedName("revenue") val revenue: Double?,
    @SerializedName("vote_count") val voteCount: Int,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("poster_path") val poster: String?,
    @SerializedName("backdrop_path") val backdrop: String
) : Serializable {
    var genresString: String = ""

    fun getReleaseDate(): String{
        return releaseDate.substringBefore("-")
    }
}