package com.llc.moviedb.repository
import com.llc.moviedb.data.model.MovieModel
import com.llc.moviedb.data.model.MoviesResponseModel
import kotlinx.coroutines.flow.Flow


interface MovieRepository {

    //for combine state flow
    val nowShowingMoviesFlow : Flow<MoviesResponseModel<MovieModel>>
    val popularMoviesFlow: Flow<MoviesResponseModel<MovieModel>>

    //for state flow
    fun getNowShowingMovies(): Flow<MoviesResponseModel<MovieModel>>
    fun getPopularMovies(): Flow<MoviesResponseModel<MovieModel>>

    //for live data
    // suspend fun getNowShowingMovies(): MoviesResponseModel<MovieModel>
   // suspend fun getPopularMovies(): MoviesResponseModel<MovieModel>

    //fun getFavourites(): Flow<List<FavouriteMovie>>
}
