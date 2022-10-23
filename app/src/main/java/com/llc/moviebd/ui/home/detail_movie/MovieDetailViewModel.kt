package com.llc.moviebd.ui.home.detail_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llc.moviebd.data.data_result.MovieDetailEvent
import com.llc.moviebd.network.MovieAPI
import kotlinx.coroutines.launch

class MovieDetailViewModel : ViewModel() {

    private val _detailUIEvent = MutableLiveData<MovieDetailEvent>()
    val detailUIEvent: LiveData<MovieDetailEvent> = _detailUIEvent

    /* private val _genres = MutableLiveData<Genre>()
     val genres: LiveData<Genre> = _genres
 */
    fun getMovieDetail(movieId: String) {

        viewModelScope.launch {
            _detailUIEvent.postValue(MovieDetailEvent.Loading)
            try {
                val result = MovieAPI.retrofitService.loadMovieDetail(movieId.toInt())
                _detailUIEvent.postValue(MovieDetailEvent.Success(result!!))
            } catch (e: Exception) {
                _detailUIEvent.postValue(MovieDetailEvent.Error(e.message.toString()))
            }
        }
    }

    fun getCredits(movieId: String) {
        viewModelScope.launch {
            _detailUIEvent.postValue(MovieDetailEvent.Loading)
            try {
                val result = MovieAPI.retrofitService.getCredits(movieId.toInt())
                _detailUIEvent.postValue(MovieDetailEvent.Credits(result))
            } catch (e: Exception) {
                _detailUIEvent.postValue(MovieDetailEvent.Error(e.message.toString()))
            }
        }
    }
}