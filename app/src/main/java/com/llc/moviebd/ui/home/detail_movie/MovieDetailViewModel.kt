package com.llc.moviebd.ui.home.detail_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llc.moviebd.data.model.MovieDetailModel
import com.llc.moviebd.data.model.MovieModel
import com.llc.moviebd.database.MovieEntity
import com.llc.moviebd.network.MovieAPI
import com.llc.moviebd.singleEvent.Event
import com.llc.moviebd.ui.home.MovieUpcomingEvent
import com.llc.myinventory.database.MovieRoomDatabase
import kotlinx.coroutines.launch

class MovieDetailViewModel : ViewModel() {

    private val _detailUIEvent = MutableLiveData<MovieDetailEvent>()
    val detailUIEvent: LiveData<MovieDetailEvent> = _detailUIEvent

    private val _favouriteAddEvent = MutableLiveData<Event<MovieDetailEvent>>()
    val favouriteAddEvent: LiveData<Event<MovieDetailEvent>> = _favouriteAddEvent


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

    fun addFavourite(
        appDatabase: MovieRoomDatabase,
        model: MovieDetailModel
    ) {
        viewModelScope.launch {
            try {
                val entity = MovieEntity(
                    id= model.id.toInt(),
                    posterPath = model.poster_path,
                    title = model.title,
                    releaseDate = model.release_date.toString(),
                    voteAverage = model.vote_average.toString()
                )
                appDatabase.movieDao().insert(entity)
                _favouriteAddEvent.postValue(Event(MovieDetailEvent.SuccessAdded("Successfully Added!")))

            } catch (e: java.lang.Exception) {
                _favouriteAddEvent.postValue(Event(MovieDetailEvent.Error(e.message.toString())))
            }
        }
    }
}