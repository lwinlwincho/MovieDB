package com.llc.moviebd.data.model

data class MovieDetailModel(

    val backdrop_path: String,
    val genres: List<Genre>,
    val id: Long,
    val imdb_id: String,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val poster_path: String,
    val release_date: String?,
    val runtime: Long,
    val title: String,
    val vote_average: Double,
    val vote_count: Long
)