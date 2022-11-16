package com.llc.moviedb.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llc.moviedb.data.model.MovieModel
import com.llc.moviedb.network.MovieAPIService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class HomeMovieListViewModel @Inject constructor(
    private val movieAPIService: MovieAPIService
) : ViewModel() {

    private val _nowShowingUiEvent = MutableLiveData<MovieUpcomingEvent>()
    val nowShowingUiEvent: LiveData<MovieUpcomingEvent> = _nowShowingUiEvent

    private val _popularUiEvent = MutableLiveData<MovieUpcomingEvent>()
    val popularUiEvent: LiveData<MovieUpcomingEvent> = _popularUiEvent

    init {
        getNowShowing()
        getPopular()
    }

    private fun getNowShowing() {

        _nowShowingUiEvent.value = MovieUpcomingEvent.Loading

        viewModelScope.launch {
            try {
                //get data from web server
                val result =
                    movieAPIService.getNowPlaying().results.sortedByDescending { it.releaseDate }
                _nowShowingUiEvent.value = MovieUpcomingEvent.Success(result)
            } catch (e: Exception) {
                _nowShowingUiEvent.value = MovieUpcomingEvent.Failure(e.message.toString())
            }
        }
    }

    private fun getPopular() {
        _popularUiEvent.value = MovieUpcomingEvent.Loading

        viewModelScope.launch {
            try {
                //get data from web server
                val popularResult =
                    movieAPIService.getPopular().results.sortedByDescending { it.vote_average }
                _popularUiEvent.value = MovieUpcomingEvent.Success(popularResult)

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