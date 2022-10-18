package com.llc.moviebd.network

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import coil.load
import com.llc.moviebd.R

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        //imgView.load(imgUri)
        imgView.load(imgUri) {
            placeholder(R.drawable.ic_baseline_double_arrow_24)
            error(R.drawable.ic_baseline_broken_image_24)
        }
    }
}