package com.llc.moviebd.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llc.moviebd.data.model.MovieModel
import kotlinx.coroutines.launch
import java.lang.Exception

//enum class MovieAPIStatus { LOADING, ERROR, DONE }

class MovieUpcomingViewModel : ViewModel() {

    private val _movieUiEvent = MutableLiveData<MovieUiEvent>()
    val movieUiEvent: LiveData<MovieUiEvent> = _movieUiEvent

    // Call getMarsPhotos() on init so we can display status immediately.
    init {
        getMoivePhoto()
    }

    private fun getMoivePhoto() {

        _movieUiEvent.value = MovieUiEvent.Loading

        viewModelScope.launch {
            try {
                //get data from web server
                val result = MovieAPI.retrofitService.getUpcomingMovie().results
                _movieUiEvent.value = MovieUiEvent.Success(result)
            } catch (e: Exception) {
                _movieUiEvent.value = MovieUiEvent.Failure(e.message.toString())
            }
        }
    }
}

//You can store many data class and singleton obj in sedled class
sealed class MovieUiEvent {
    data class Success(val movieList: List<MovieModel>) : MovieUiEvent()
    data class Failure(val message: String) : MovieUiEvent()
    object Loading : MovieUiEvent()
}