package com.llc.moviebd.network

import com.squareup.moshi.Json

data class MoviePhoto(
    /**
     * This data class defines a Mars photo which includes an ID, and the image URL.
     * The property names of this data class are used by Moshi to match the names of values in JSON.
     */
   /* val id:String
    val img_src:String*/

    val id: String,
    @Json(name = "img_src") val imgSrcUrl: String
)
