package com.llc.moviedb.data.model

import com.squareup.moshi.Json
import java.io.Serializable

data class MovieModel(
    @Json(name = "id") val id: Int,
    //poster path can string or null
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "title") val title: String,
    @Json(name = "release_date") val releaseDate: String,
    @Json(name = "vote_average") val vote_average: Double
) : Serializable

