package com.llc.moviedb.dataa.remoteDataSource.localDataSource

import com.llc.moviedb.datastore.Movie

interface LocalDataSource {

   // fun getFavourites(): Flow<List<FavouriteMovie>>

   suspend fun saveNowPlaying(movieModel: List<Movie>)

}