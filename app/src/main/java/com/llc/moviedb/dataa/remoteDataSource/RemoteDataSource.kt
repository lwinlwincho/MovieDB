package com.llc.moviedb.dataa.remoteDataSource

import com.llc.moviedb.data.model.MovieModel
import com.llc.moviedb.data.model.MoviesResponseModel
import com.llc.moviedb.datastore.Movie
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {

    suspend fun getNowPlaying(): Flow<MoviesResponseModel<MovieModel>>

   // suspend fun getNowPlaying(): MoviesResponseModel<MovieModel>

    //suspend fun getPopular(): MoviesResponseModel<MovieModel>

    suspend fun getPopular(): Flow<MoviesResponseModel<MovieModel>>

}