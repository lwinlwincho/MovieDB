package com.llc.moviedb.repository

import android.net.ConnectivityManager
import com.llc.moviedb.data.model.MovieModel
import com.llc.moviedb.data.model.MoviesResponseModel
import com.llc.moviedb.dataa.remoteDataSource.RemoteDataSource
import com.llc.moviedb.dataa.remoteDataSource.localDataSource.LocalDataSource
import com.llc.moviedb.datastore.Movie
import com.llc.moviedb.extension.isNetworkAvailable
import com.llc.moviedb.network.MovieAPIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieAPIService: MovieAPIService,
    private val remoteDataSource: RemoteDataSource,
    private val connectivityManager: ConnectivityManager,
    private val localDataSource: LocalDataSource
) : MovieRepository {

    private val isOnline get() = connectivityManager.isNetworkAvailable()

    override suspend fun getNowShowingMovies(): Flow<MoviesResponseModel<MovieModel>> {
        /*return remoteDataSource.getNowPlaying().onEach {
            val movieModelList: List<MovieModel> = it.results
            val movieList: List<Movie> = movieModelList.map {model ->
                Movie(
                    id = model.id,
                    posterPath = model.posterPath,
                    title = model.title,
                    releaseDate = model.releaseDate,
                    vote_average = model.vote_average
                )
            }
            saveInCache(movieList)
        }*/

        return remoteDataSource.getNowPlaying()

    }

    override suspend fun getPopularMovies(): Flow<MoviesResponseModel<MovieModel>> {
/*        return remoteDataSource.getPopular().onEach {
            val movieModelList: List<MovieModel> = it.results
            val movieList: List<Movie> = movieModelList.map {model ->
                Movie(
                    id = model.id,
                    posterPath = model.posterPath,
                    title = model.title,
                    releaseDate = model.releaseDate,
                    vote_average = model.vote_average
                )
            }
            saveInCache(movieList)
        }*/

        return remoteDataSource.getPopular()
    }


    /*    override suspend fun getNowShowingMovies(): MoviesResponseModel<MovieModel>{
            return remoteDataSource.getNowPlaying()
        }*/

/*
    override suspend fun getPopularMovies(): MoviesResponseModel<MovieModel> {
        return remoteDataSource.getPopular()
    }
*/

    private suspend fun saveInCache(movieModels: List<Movie>) {
        localDataSource.saveNowPlaying(movieModels)
    }

    /*override fun getFavourites(): Flow<List<FavouriteMovie>> {
       return localDataSource.getFavourites()
    }
*/
}