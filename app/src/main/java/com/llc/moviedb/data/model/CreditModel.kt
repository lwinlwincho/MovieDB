package com.llc.moviedb.data.model

import com.squareup.moshi.Json

data class CreditModel(
    @Json(name = "id") val id: Int,
    @Json(name = "cast") val cast: List<CastModel>
)
