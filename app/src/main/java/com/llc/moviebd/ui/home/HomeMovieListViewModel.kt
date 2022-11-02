package com.llc.moviebd.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llc.moviebd.data.model.MovieModel
import com.llc.moviebd.database.MovieEntity
import com.llc.moviebd.network.MovieAPI
import com.llc.moviebd.singleEvent.Event
import com.llc.myinventory.database.MovieRoomDatabase
import kotlinx.coroutines.launch
import java.lang.Exception

class HomeMovieListViewModel : ViewModel() {

    private val _nowShowingUiEvent = MutableLiveData<MovieUpcomingEvent>()
    val nowShowingUiEvent: LiveData<MovieUpcomingEvent> = _nowShowingUiEvent

    private val _popularUiEvent = MutableLiveData<MovieUpcomingEvent>()
    val popularUiEvent: LiveData<MovieUpcomingEvent> = _popularUiEvent

    private val _favouriteUiEvent = MutableLiveData<Event<MovieUpcomingEvent>>()
    val favouriteUiEvent: LiveData<Event<MovieUpcomingEvent>> = _favouriteUiEvent

    // Call getMarsPhotos() on init so we can display status immediately.
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
                    MovieAPI.retrofitService.getNowPlaying().results.sortedByDescending { it.releaseDate }
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
                    MovieAPI.retrofitService.getPopular().results.sortedByDescending { it.vote_average }
                _popularUiEvent.value = MovieUpcomingEvent.Success(popularResult)

            } catch (e: Exception) {
                _popularUiEvent.value = MovieUpcomingEvent.Failure(e.message.toString())
            }
        }
    }

    fun addFavourite(
        appDatabase: MovieRoomDatabase,
        model: MovieModel
    ) {
        viewModelScope.launch {
            try {
                val entity = MovieEntity(
                    id= model.id,
                    posterPath = model.posterPath.orEmpty(),
                    title = model.title,
                    releaseDate = model.releaseDate,
                    voteAverage = model.vote_average.toString()
                )
                appDatabase.movieDao().insert(entity)
                _favouriteUiEvent.postValue(Event(MovieUpcomingEvent.SuccessAddedSms("Successfully Added!")))

            } catch (e: Exception) {
                _favouriteUiEvent.postValue(Event(MovieUpcomingEvent.Failure(e.message.toString())))
            }
        }
    }
}

//You can store many data class and singleton obj in sealed class
sealed class MovieUpcomingEvent {
    data class Success(val movieList: List<MovieModel>) : MovieUpcomingEvent()
    data class SuccessRemovedSms(val message: String) : MovieUpcomingEvent()
    data class SuccessAddedSms(val message: String) : MovieUpcomingEvent()
    data class Failure(val message: String) : MovieUpcomingEvent()
    object Loading : MovieUpcomingEvent()
}