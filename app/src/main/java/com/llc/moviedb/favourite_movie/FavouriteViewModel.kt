package com.llc.moviedb.favourite_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llc.moviedb.database.FavouriteMovieEntity
import com.llc.moviedb.database.MovieDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val movieDao: MovieDao
) : ViewModel() {

    private val _favouriteUiEvent = MutableLiveData<FavouriteEvent>()
    val favouriteUiEvent: LiveData<FavouriteEvent> = _favouriteUiEvent

    fun getAllFavourite() {

        viewModelScope.launch {
            try {
                //get data from offline database
                val result: List<FavouriteMovieEntity> = movieDao.getAllFavouriteMovie()
                _favouriteUiEvent.value = FavouriteEvent.Success(result)
            } catch (e: Exception) {
                _favouriteUiEvent.value = FavouriteEvent.Failure(e.message.toString())
            }
        }
    }
}

sealed class FavouriteEvent {
    data class Success(val movieList: List<FavouriteMovieEntity>) : FavouriteEvent()
    data class Failure(val message: String) : FavouriteEvent()
}