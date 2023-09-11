package com.llc.moviedb.repository
import com.llc.moviedb.data.model.MovieModel
import com.llc.moviedb.data.model.MoviesResponseModel
import com.llc.moviedb.datastore.Movie
import kotlinx.coroutines.flow.Flow


interface MovieRepository {
    suspend fun getNowShowingMovies(): Flow<MoviesResponseModel<MovieModel>>

    suspend fun getPopularMovies(): Flow<MoviesResponseModel<MovieModel>>

    // suspend fun getNowShowingMovies(): MoviesResponseModel<MovieModel>
   // suspend fun getPopularMovies(): MoviesResponseModel<MovieModel>

    //fun getFavourites(): Flow<List<FavouriteMovie>>
}
