package com.lucasrodrigues.themovieranking.view

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.lucasrodrigues.themovieranking.R
import com.lucasrodrigues.themovieranking.databinding.MovieDetailFragmentBinding
import com.lucasrodrigues.themovieranking.util.TmdbInterface
import com.lucasrodrigues.themovieranking.viewmodel.MovieDetailViewModel
import kotlinx.android.synthetic.main.movie_detail_fragment.*


class MovieDetailFragment : Fragment() {

    private lateinit var viewModel: MovieDetailViewModel
    private lateinit var binding: MovieDetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MovieDetailFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.dataLoaded = false
        viewModel = ViewModelProviders.of(this).get(MovieDetailViewModel::class.java)
        viewModel.movie.observe(viewLifecycleOwner, Observer {
            binding.movie = it
            binding.imagesBaseUrl = TmdbInterface.imagesBaseURL
            binding.dataLoaded = true
        })
        val safeArgs: MovieDetailFragmentArgs by navArgs()

        if (checkInternetConnection())
            viewModel.getMovieDetail(safeArgs.NavigateToDetails)
        else
            connectionError()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbar2)
        toolbar2.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar2.setNavigationOnClickListener { activity!!.onBackPressed() }
        nestedScrollView.setOnScrollChangeListener(scrollViewListener())
    }

    fun scrollViewListener(): NestedScrollView.OnScrollChangeListener {
        return NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (v.canScrollVertically(-1)) {
                toolbar2.setBackgroundColor(
                    resources.getColor(
                        R.color.colorPrimary,
                        (activity as AppCompatActivity).theme
                    )
                )
            } else {
                toolbar2.setBackgroundColor(
                    resources.getColor(
                        android.R.color.transparent,
                        (activity as AppCompatActivity).theme
                    )
                )
            }
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
