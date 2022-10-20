package com.llc.moviebd.data.model

data class MovieDetailModel(

    val backdrop_path: String,

    val id: Long,
    val poster_path: String,
    val release_date: String?,
    val status: String,
    val title: String
)