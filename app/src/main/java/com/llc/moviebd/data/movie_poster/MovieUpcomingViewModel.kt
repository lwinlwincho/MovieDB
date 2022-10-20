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

    private val _uiEvent = MutableLiveData<MovieEvent>()
    val uiEvent: LiveData<MovieEvent> = _uiEvent

    // Call getMarsPhotos() on init so we can display status immediately.
    init {
        getMoivePhoto()
    }

    private fun getMoivePhoto() {

        _uiEvent.value = MovieEvent.Loading

        viewModelScope.launch {
            try {
                //get data from web server
                _uiEvent.value =
                    MovieEvent.Success(MovieAPI.retrofitService.getUpcomingMovie().results)
            } catch (e: Exception) {
                _uiEvent.value = MovieEvent.Failure(e.message.toString())
            }
        }
    }
}

//You can store many data class and singleton obj in sedled class
sealed class MovieEvent {
    data class Success(val movieList: List<MovieModel>) : MovieEvent()
    data class Failure(val message: String) : MovieEvent()
    object Loading : MovieEvent()
}