package com.llc.moviedb.ui.home.seeMore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llc.moviedb.data.model.MovieModel
import com.llc.moviedb.network.MovieAPIService
import com.llc.moviedb.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SeeMoreViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _seeMoreUiEvent = MutableLiveData<SeeMoreEvent>()
    val seeMoreUiEvent: LiveData<SeeMoreEvent> = _seeMoreUiEvent

    fun getNowShowing() {
        _seeMoreUiEvent.value = SeeMoreEvent.Loading
        viewModelScope.launch {
            try {
                //get data from web server
                val nowShowingResult =
                    movieRepository.getNowShowingMovies().results.sortedByDescending { it.releaseDate }
                _seeMoreUiEvent.value = SeeMoreEvent.Success(nowShowingResult)
            } catch (e: Exception) {
                _seeMoreUiEvent.value = SeeMoreEvent.Failure(e.message.toString())
            }
        }
    }

    fun getPopular() {
        _seeMoreUiEvent.value = SeeMoreEvent.Loading
        viewModelScope.launch {
            try {
                //get data from web server
                val popularResult =
                    movieRepository.getPopularMovies().results.sortedByDescending { it.vote_average }
                _seeMoreUiEvent.value = SeeMoreEvent.Success(popularResult)

            } catch (e: Exception) {
                _seeMoreUiEvent.value = SeeMoreEvent.Failure(e.message.toString())
            }
        }
    }
}

sealed class SeeMoreEvent {
    data class Success(val movieList: List<MovieModel>) : SeeMoreEvent()
    data class Failure(val message: String) : SeeMoreEvent()
    object Loading : SeeMoreEvent()
}