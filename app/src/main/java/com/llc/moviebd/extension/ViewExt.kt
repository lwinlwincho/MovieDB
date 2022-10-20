package com.llc.moviebd.extension

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.llc.moviebd.R

fun ImageView.loadFromUrl(url: String?) =
    if (url != null) {
        Glide.with(this.context.applicationContext)
            .load(url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
    } else {
        Glide.with(this.context.applicationContext)
            .load(R.drawable.ic_launcher_background)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
    }