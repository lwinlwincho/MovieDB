package com.llc.moviedb.data.model

data class MoviesResponseModel<N>(
    val page: Int,
    val results: List<N>,
    val total_pages: Int,
    val total_results: Int
)