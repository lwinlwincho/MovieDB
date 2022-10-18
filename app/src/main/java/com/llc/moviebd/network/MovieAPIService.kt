package com.llc.moviebd.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL =
    "https://android-kotlin-fun-mars-server.appspot.com"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

//The Retrofit object with the Moshi converter.
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface MovieAPIService {
    @GET("photos")
    suspend fun getPhoto(): List<MoviePhoto>
}

object MovieAPI {
    val retrofitService: MovieAPIService by lazy {
        retrofit.create(MovieAPIService::class.java)
    }
}

