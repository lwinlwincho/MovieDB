package com.llc.moviebd.data.model

import com.squareup.moshi.Json

data class CastModel(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "original_name") val original_name: String,
    @Json(name = "profile_path") val profile_path: String?
)
