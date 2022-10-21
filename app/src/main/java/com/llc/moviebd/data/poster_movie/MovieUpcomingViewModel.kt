package com.llc.moviebd.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llc.moviebd.data.model.MovieModel
import kotlinx.coroutines.launch
import java.lang.Exception

class MovieUpcomingViewModel : ViewModel() {

    private val _movieUiEvent = MutableLiveData<MovieUpcomingEvent>()
    val movieUiEvent: LiveData<MovieUpcomingEvent> = _movieUiEvent

    // Call getMarsPhotos() on init so we can display status immediately.
    init {
        getMoivePhoto()
    }

    private fun getMoivePhoto() {

        _movieUiEvent.value = MovieUpcomingEvent.Loading

        viewModelScope.launch {
            try {
                //get data from web server
                val result = MovieAPI.retrofitService.getUpcomingMovie().results
                _movieUiEvent.value = MovieUpcomingEvent.Success(result)
            } catch (e: Exception) {
                _movieUiEvent.value = MovieUpcomingEvent.Failure(e.message.toString())
            }
        }
    }
}

//You can store many data class and singleton obj in sealed class
sealed class MovieUpcomingEvent {
    data class Success(val movieList: List<MovieModel>) : MovieUpcomingEvent()
    data class Failure(val message: String) : MovieUpcomingEvent()
    object Loading : MovieUpcomingEvent()
}