package com.lucasrodrigues.themovieranking.util

import com.google.gson.annotations.SerializedName
import com.lucasrodrigues.themovieranking.model.Genre

data class GenresResponseContainer (
    @SerializedName("genres") val genres: List<Genre>
)