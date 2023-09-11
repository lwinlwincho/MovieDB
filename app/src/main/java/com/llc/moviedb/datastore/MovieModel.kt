package com.llc.moviedb.datastore

import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val id: Int = 0,
    val posterPath: String? = "",
    val title: String = "",
    val releaseDate: String = "",
    val vote_average: Double = 0.0
)

@Serializable
data class MovieList(
    val nowShowing: List<Movie> = emptyList(),
    val popular: List<Movie> = emptyList()
)