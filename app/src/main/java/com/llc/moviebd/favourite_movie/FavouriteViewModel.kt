package com.llc.moviebd.favourite_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llc.moviebd.data.model.MovieModel
import com.llc.moviebd.network.MovieAPI
import kotlinx.coroutines.launch
import java.lang.Exception

class FavouriteViewModel : ViewModel() {
    private val _favouriteUEvent = MutableLiveData<FavouriteEvent>()
    val favouriteUEvent: LiveData<FavouriteEvent> = _favouriteUEvent

    fun getFavourite() {
        _favouriteUEvent.value = FavouriteEvent.Loading
        viewModelScope.launch {
            try {
                //get data from web server
                val result =
                    MovieAPI.retrofitService.getNowPlaying().results.sortedByDescending { it.releaseDate }
                _favouriteUEvent.value = FavouriteEvent.Success(result)
            } catch (e: Exception) {
                _favouriteUEvent.value = FavouriteEvent.Failure(e.message.toString())
            }
        }
    }
}

sealed class FavouriteEvent {
    data class Success(val movieList: List<MovieModel>) : FavouriteEvent()
    data class SuccessfulAdded(val message: String) : FavouriteEvent()
    data class SuccessfulRemoved(val message: String) : FavouriteEvent()
    data class Failure(val message: String) : FavouriteEvent()
    object Loading : FavouriteEvent()
}