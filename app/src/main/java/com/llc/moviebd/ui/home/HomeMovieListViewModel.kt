package com.llc.moviebd.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llc.moviebd.data.model.MovieModel
import kotlinx.coroutines.launch
import java.lang.Exception

class HomeMovieListViewModel : ViewModel() {

    private val _movieUiEvent = MutableLiveData<MovieUpcomingEvent>()
    val movieUiEvent: LiveData<MovieUpcomingEvent> = _movieUiEvent

    private val _popularUiEvent = MutableLiveData<MovieUpcomingEvent>()
    val popularUiEvent: LiveData<MovieUpcomingEvent> = _popularUiEvent

    // Call getMarsPhotos() on init so we can display status immediately.
    init {
        getMovie()
        getPopular()
    }

    private fun getMovie() {

        _movieUiEvent.value = MovieUpcomingEvent.Loading

        viewModelScope.launch {
            try {
                //get data from web server
                val result = MovieAPI.retrofitService.getNowPlaying().results
                _movieUiEvent.value = MovieUpcomingEvent.Success(result)

               /* val popularResult=MovieAPI.retrofitService.getPopular().results
                _popularUiEvent.value=MovieUpcomingEvent.Success(popularResult)
*/
            } catch (e: Exception) {
                _movieUiEvent.value = MovieUpcomingEvent.Failure(e.message.toString())
            }
        }
    }

    private fun getPopular() {
        _popularUiEvent.value = MovieUpcomingEvent.Loading

        viewModelScope.launch {
            try {
                //get data from web server

                val popularResult=MovieAPI.retrofitService.getPopular().results
                _popularUiEvent.value=MovieUpcomingEvent.Success(popularResult)

            } catch (e: Exception) {
                _popularUiEvent.value = MovieUpcomingEvent.Failure(e.message.toString())
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