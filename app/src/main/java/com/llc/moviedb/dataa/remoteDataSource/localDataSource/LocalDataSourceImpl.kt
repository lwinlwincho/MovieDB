package com.llc.moviedb.dataa.remoteDataSource.localDataSource

import android.content.Context
import androidx.datastore.dataStore
import com.llc.moviedb.datastore.Movie
import com.llc.moviedb.datastore.MovieListSerializer
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

val Context.dataStore by dataStore("movie-settings.json", MovieListSerializer)

class LocalDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
    ): LocalDataSource {

/*
    override fun getFavourites(): Flow<List<FavouriteMovie>> {
        return context.dataStore.data.map{
            listOf(it)
        }
    }
*/

    override suspend fun saveNowPlaying(movieModels: List<Movie>) {
        context.dataStore.updateData {
            it.copy(
                nowShowing = movieModels
            )
        }
    }
}



/*
private suspend fun setLanguage(language: Language) {
    dataStore.updateData {
        it.copy(
            language = language
        )
    }
}*/
