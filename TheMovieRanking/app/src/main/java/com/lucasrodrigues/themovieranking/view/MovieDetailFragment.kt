package com.lucasrodrigues.themovieranking.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.lucasrodrigues.themovieranking.R
import com.lucasrodrigues.themovieranking.viewmodel.MovieDetailViewModel
import kotlinx.android.synthetic.main.movie_detail_fragment.*
import kotlinx.android.synthetic.main.movies_fragment.*
import kotlinx.android.synthetic.main.movies_fragment.toolbar


class MovieDetailFragment : Fragment() {

    private lateinit var viewModel: MovieDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.movie_detail_fragment, container, false)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MovieDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
