package com.llc.moviedb.dataa.remoteDataSource

import com.llc.moviedb.data.model.MovieModel
import com.llc.moviedb.data.model.MoviesResponseModel
import com.llc.moviedb.datastore.Movie
import com.llc.moviedb.network.MovieAPIService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val movieAPIService: MovieAPIService
) : RemoteDataSource {

    override suspend fun getNowPlaying(): Flow<MoviesResponseModel<MovieModel>> {
        return flow { emit(movieAPIService.getNowPlaying()) }
    }

    override suspend fun getPopular(): Flow<MoviesResponseModel<MovieModel>> {
        return flow { emit(movieAPIService.getPopular()) }
    }

/*    override suspend fun getNowPlaying(): MoviesResponseModel<MovieModel>{
        return movieAPIService.getNowPlaying()
    }*/

/*    override suspend fun getPopular(): MoviesResponseModel<MovieModel> {
        return movieAPIService.getPopular()
    }*/
}