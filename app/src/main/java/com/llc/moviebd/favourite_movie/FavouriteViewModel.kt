package com.llc.moviebd.favourite_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llc.moviebd.database.FavouriteMovieEntity
import com.llc.moviebd.database.MovieDao
import com.llc.moviebd.database.MovieRoomDatabase
import kotlinx.coroutines.launch
import java.lang.Exception

class FavouriteViewModel : ViewModel() {

    private lateinit var movieDao:MovieDao

    private val _favouriteUEvent = MutableLiveData<FavouriteEvent>()
    val favouriteUEvent: LiveData<FavouriteEvent> = _favouriteUEvent

    fun setAppDatabase(appDatabase: MovieRoomDatabase) {
        this.movieDao = appDatabase.movieDao()
    }

    fun getAllFavourite() {

        viewModelScope.launch {
            try {
                //get data from offline database
                val result: List<FavouriteMovieEntity> = movieDao.getAllFavouriteMovie()
                _favouriteUEvent.value = FavouriteEvent.Success(result)
            } catch (e: Exception) {
                _favouriteUEvent.value = FavouriteEvent.Failure(e.message.toString())
            }
        }
    }
}

sealed class FavouriteEvent {
    data class Success(val movieList: List<FavouriteMovieEntity>) : FavouriteEvent()
    data class Failure(val message: String) : FavouriteEvent()
}