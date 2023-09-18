package com.llc.moviedb.dataa.remoteDataSource

import com.llc.moviedb.data.model.MovieModel
import com.llc.moviedb.data.model.MoviesResponseModel
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {

    //for combine state flow
    val nowShowingMoviesFlow: Flow<MoviesResponseModel<MovieModel>>
    val popularMoviesFlow: Flow<MoviesResponseModel<MovieModel>>

    //for live data
    // suspend fun getNowPlaying(): MoviesResponseModel<MovieModel>
    //suspend fun getPopular(): MoviesResponseModel<MovieModel>

    // for state flow
    fun getNowPlaying(): Flow<MoviesResponseModel<MovieModel>>
    fun getPopular(): Flow<MoviesResponseModel<MovieModel>>

}