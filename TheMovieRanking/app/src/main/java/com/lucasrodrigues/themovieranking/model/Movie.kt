package com.lucasrodrigues.themovieranking.model

import com.google.gson.annotations.SerializedName
import com.lucasrodrigues.themovieranking.util.TmdbInterface
import java.io.Serializable
import java.text.NumberFormat

data class Movie(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("original_title") val originalTitle: String,
    @SerializedName("original_language") val originalLanguage: String,
    @SerializedName("tagline") val tagline: String,
    @SerializedName("genre_ids") val genres: List<Int>,
    @SerializedName("overview") val overview: String,
    @SerializedName("release_date") private val releaseDate: String?,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("budget") val budget: Long?,
    @SerializedName("revenue") val revenue: Long?,
    @SerializedName("vote_count") val voteCount: Int,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("poster_path") val poster: String?,
    @SerializedName("backdrop_path") val backdrop: String
) : Serializable {
    var genresString: String = ""

    fun getReleaseDate(): String {
        when (releaseDate.isNullOrEmpty()) {
            true -> return ""
            false -> return releaseDate.substringBefore("-")
        }
    }

    fun getBudget(): String{
        return NumberFormat.getCurrencyInstance().format(budget)
    }

    fun getRevenue(): String{
        return NumberFormat.getCurrencyInstance().format(revenue)
    }
}