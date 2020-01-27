package com.lucasrodrigues.themovieranking.util

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

@BindingAdapter("bindResource")
fun imageDatabindFromUrl(imageView: ImageView, url: String) {
    if(url.isNotEmpty())
        Picasso.get().load(url).into(imageView)
}

@BindingAdapter("bindVisibility")
fun viewVisibility(view: View, visible: Boolean) {
    if(visible)
        view.visibility = View.VISIBLE;
    else
        view.visibility = View.INVISIBLE
}

