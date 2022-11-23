package com.llc.moviedb.repository

import com.llc.moviedb.data.model.MovieModel
import com.llc.moviedb.data.model.MoviesResponseModel
import com.llc.moviedb.network.API_KEY
import com.llc.moviedb.network.MovieAPIService
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieAPIService: MovieAPIService
) : MovieRepository {

    override suspend fun getNowShowingMovies(): MoviesResponseModel<MovieModel> {
        return movieAPIService.getNowPlaying()
    }

    override suspend fun getPopularMovies(): MoviesResponseModel<MovieModel> {
        return movieAPIService.getPopular(api_key = API_KEY)
    }
}