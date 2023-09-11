package com.llc.moviedb.favourite_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llc.moviedb.database.FavouriteMovieEntity
import com.llc.moviedb.database.MovieDao
import com.llc.moviedb.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val movieDao: MovieDao,
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _favouriteUiEvent = MutableLiveData<LatestNewsUiState>()
    val favouriteUiEvent: LiveData<LatestNewsUiState> = _favouriteUiEvent

    private val _favUiState = MutableStateFlow(LatestNewsUiState.Success(emptyList()))
    val favUiState: StateFlow<LatestNewsUiState> = _favUiState

    fun getAllFavourite() {

        viewModelScope.launch {
            try {
               // get data from offline database
                  val result: List<FavouriteMovieEntity> = movieDao.getAllFavouriteMovie()
                  _favouriteUiEvent.value = LatestNewsUiState.Success(result)

               // movieRepository.getFavourites().collect {
                  //  _favUiState.value = LatestNewsUiState.Success(it)
              //  }
            } catch (e: Exception) {
                _favouriteUiEvent.value = LatestNewsUiState.Failure(e.message.toString())
            }
        }
    }
}

sealed class LatestNewsUiState {
    data class Success(val movieList: List<FavouriteMovieEntity>) : LatestNewsUiState()

   // data class Success(val movieList: List<FavouriteMovie>) : LatestNewsUiState()
    data class Failure(val message: String) : LatestNewsUiState()
}