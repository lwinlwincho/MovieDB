package com.llc.moviebd.ui.home

import android.icu.text.CaseMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llc.moviebd.data.model.MovieModel
import com.llc.moviebd.database.MovieEntity
import com.llc.moviebd.favourite_movie.FavouriteEvent
import com.llc.moviebd.network.MovieAPI
import com.llc.myinventory.database.MovieRoomDatabase
import kotlinx.coroutines.launch
import java.lang.Exception

class HomeMovieListViewModel : ViewModel() {

    private val _nowShowingUiEvent = MutableLiveData<MovieUpcomingEvent>()
    val nowShowingUiEvent: LiveData<MovieUpcomingEvent> = _nowShowingUiEvent

    private val _popularUiEvent = MutableLiveData<MovieUpcomingEvent>()
    val popularUiEvent: LiveData<MovieUpcomingEvent> = _popularUiEvent

    private val _favouriteUiEvent = MutableLiveData<FavouriteEvent>()
    val favouriteUiEvent: LiveData<FavouriteEvent> = _favouriteUiEvent

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
        posterPath: String,
        title: String,
        releaseDate: String,
        voteAverage: String,
    ) {
        viewModelScope.launch {
            try {
                val entity = MovieEntity(
                    posterPath = posterPath,
                    title = title,
                    releaseDate = releaseDate,
                    voteAverage =voteAverage
                )
                appDatabase.movieDao().insert(entity)
                _favouriteUiEvent.postValue(FavouriteEvent.SuccessfulAdded("Successfully Added!"))
            } catch (e: Exception) {
                _favouriteUiEvent.postValue(FavouriteEvent.Failure(e.message.toString()))
            }
        }
    }
}

//You can store many data class and singleton obj in sealed class
sealed class MovieUpcomingEvent {
    data class Success(val movieList: List<MovieModel>) : MovieUpcomingEvent()
    data class SuccessFavSms(val message: String) : MovieUpcomingEvent()
    data class Failure(val message: String) : MovieUpcomingEvent()
    object Loading : MovieUpcomingEvent()
}