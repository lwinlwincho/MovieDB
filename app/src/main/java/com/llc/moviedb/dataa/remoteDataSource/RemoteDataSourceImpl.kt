package com.llc.moviedb.dataa.remoteDataSource

import com.llc.moviedb.data.model.MovieModel
import com.llc.moviedb.data.model.MoviesResponseModel
import com.llc.moviedb.network.MovieAPIService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val movieAPIService: MovieAPIService
) : RemoteDataSource {

    //for combine state flow
    override val nowShowingMoviesFlow: Flow<MoviesResponseModel<MovieModel>>
        get() = flow { emit(movieAPIService.getNowPlaying()) }
    override val popularMoviesFlow: Flow<MoviesResponseModel<MovieModel>>
        get() = flow { emit(movieAPIService.getPopular()) }


    //for state flow
    override fun getNowPlaying(): Flow<MoviesResponseModel<MovieModel>> {
        return flow { emit(movieAPIService.getNowPlaying()) }
    }

    override fun getPopular(): Flow<MoviesResponseModel<MovieModel>> {
        return flow { emit(movieAPIService.getPopular()) }
    }

    //for live data
    /*    override fun getNowPlaying(): MoviesResponseModel<MovieModel>{
            return movieAPIService.getNowPlaying()
        }
          override fun getPopular(): MoviesResponseModel<MovieModel> {
            return movieAPIService.getPopular()
        }*/
}