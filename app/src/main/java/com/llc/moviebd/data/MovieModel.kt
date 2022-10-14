package com.llc.moviebd.data

import java.io.Serializable

data class MovieModel(
    val image: Int,
    val title: Int,
    val date: Int
) : Serializable
