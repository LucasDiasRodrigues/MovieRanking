package com.lucasrodrigues.themovieranking.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lucasrodrigues.themovieranking.model.Movie

class MovieDetailViewModel : ViewModel() {
    val movies = MutableLiveData<List<Movie>>()


}
