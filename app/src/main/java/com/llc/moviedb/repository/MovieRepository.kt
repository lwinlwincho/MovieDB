package com.llc.moviedb.repository

import com.llc.moviedb.data.model.MovieModel
import com.llc.moviedb.data.model.MoviesResponseModel

interface MovieRepository {
    //suspend fun getMarsPhotos(): List<MovieModel>
    suspend fun getNowShowingMovies(): MoviesResponseModel<MovieModel>
    suspend fun getPopularMovies(): MoviesResponseModel<MovieModel>
}
