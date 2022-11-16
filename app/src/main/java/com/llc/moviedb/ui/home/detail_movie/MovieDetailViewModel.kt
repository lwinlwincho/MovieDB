package com.llc.moviedb.ui.home.detail_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llc.moviedb.data.model.MovieDetailModel
import com.llc.moviedb.database.FavouriteMovieEntity
import com.llc.moviedb.database.MovieDao
import com.llc.moviedb.network.MovieAPIService
import com.llc.moviedb.singleEvent.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieDao: MovieDao, private val movieAPIService: MovieAPIService
) : ViewModel() {

    private val _detailUIEvent = MutableLiveData<MovieDetailEvent>()
    val detailUIEvent: LiveData<MovieDetailEvent> = _detailUIEvent

    private val _favouriteEvent = MutableLiveData<Event<MovieDetailEvent>>()
    val favouriteEvent: LiveData<Event<MovieDetailEvent>> = _favouriteEvent

    private val _favouriteStatusEvent = MutableLiveData<Event<Boolean>>()
    val favouriteStatusEvent: LiveData<Event<Boolean>> = _favouriteStatusEvent

    fun getMovieDetail(movieId: String) {
        viewModelScope.launch {
            _detailUIEvent.postValue(MovieDetailEvent.Loading)
            try {
                val result = movieAPIService.loadMovieDetail(movieId.toInt())
                _detailUIEvent.postValue(MovieDetailEvent.Success(result!!))
            } catch (e: Exception) {
                _detailUIEvent.postValue(MovieDetailEvent.Error(e.message.toString()))
            }
        }

        viewModelScope.launch {
            _detailUIEvent.postValue(MovieDetailEvent.Loading)
            try {
                val result = movieAPIService.getCredits(movieId.toInt())
                _detailUIEvent.postValue(MovieDetailEvent.Credits(result))
            } catch (e: Exception) {
                _detailUIEvent.postValue(MovieDetailEvent.Error(e.message.toString()))
            }
        }
    }

    private fun addFavourite(model: MovieDetailModel) {
        viewModelScope.launch {
            try {
                val entity = FavouriteMovieEntity(
                    id = model.id,
                    posterPath = model.poster_path,
                    title = model.title,
                    releaseDate = model.release_date.toString(),
                    voteAverage = model.vote_average.toString()
                )
                movieDao.insertFavouriteMovie(entity)
                _favouriteEvent.postValue(Event(MovieDetailEvent.SuccessAdded("Added Bookmark!")))

            } catch (e: java.lang.Exception) {
                _favouriteEvent.postValue(Event(MovieDetailEvent.Error(e.message.toString())))
            }
        }
    }

    private fun removeFavourite(item: MovieDetailModel) {
        viewModelScope.launch {
            try {
                val entity = FavouriteMovieEntity(
                    id = item.id,
                    posterPath = item.poster_path,
                    title = item.title,
                    releaseDate = item.release_date.toString(),
                    voteAverage = item.vote_average.toString()
                )
                movieDao.delete(entity)
                _favouriteEvent.postValue(Event(MovieDetailEvent.SuccessRemoved("Removed Bookmark!")))
            } catch (e: Exception) {
                _favouriteEvent.postValue(Event(MovieDetailEvent.Error(e.message.toString())))
            }
        }
    }

    fun checkFavouriteMovie(id: Long) {
        viewModelScope.launch {
            val favouriteMovie = movieDao.getFavouriteMovieById(id)
            val isFavourite = favouriteMovie?.id == id
            _favouriteStatusEvent.postValue(Event(isFavourite))
        }
    }

    fun toggleFavourite(detailDataModel: MovieDetailModel) {
        viewModelScope.launch {
            val favouriteMovie = movieDao.getFavouriteMovieById(detailDataModel.id)
            val isFavourite = favouriteMovie?.id == detailDataModel.id

            if (isFavourite) {
                removeFavourite(detailDataModel)
            } else {
                addFavourite(detailDataModel)
            }
        }
    }
}